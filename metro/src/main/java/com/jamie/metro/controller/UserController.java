package com.jamie.metro.controller;

import com.jamie.metro.dto.*;
import com.jamie.metro.entity.Station;
import com.jamie.metro.service.AuthenticationService;
import com.jamie.metro.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@AllArgsConstructor
@Controller
public class UserController {

    private AuthenticationService authenticationService;
    private UserService userService;

    private static List<TransactionDto> getTransactionsDto(int page, List<TransactionDto> transactionList, int pageSize) {
        return transactionList.stream()
                .skip((long) page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    private static ModelAndView getModelAndView(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            // If no user found, redirect to the login page
            return new ModelAndView("redirect:/login");
        }
        return null;
    }

    // General Section
    @ModelAttribute("stationNames")
    // Station List is stored locally, just for testing.
    List<String> getStations() {
        return Arrays.asList("Station 1", "Station 2", "Station 3", "Station 4");
    }

    // Redirect The Base URL to Login.
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:login";  // Redirects the user to the "/login" page
    }

    // Sign Up Section
    @RequestMapping("signUp")
    public ModelAndView getSignUpPage() {
        return new ModelAndView("User/SignUp", "user", new RegisterDto());
    }

    // Check For Submit / Sign Up Check
    @PostMapping("signUp")
    public String registerCheck(@ModelAttribute("user") RegisterDto user, RedirectAttributes redirectAttributes) {
        // Handle the registration logic
        String response = authenticationService.register(user);
        System.out.println(response);

        // Check for errors
        if (response.startsWith("Error:")) {
            // Pass the error message as a flash attribute
            redirectAttributes.addFlashAttribute("message", response);
            // Redirect back to the sign-up page
            return "redirect:signUp";
        }

        // Redirect to login on success
        redirectAttributes.addFlashAttribute("message", response);
        return "redirect:/login"; // Adjust this to match your login URL
    }

    // Login Section
    @RequestMapping("login")
    public ModelAndView LoginPage() {
        return new ModelAndView("User/Login", "user", new LoginDto());
    }

    // Check For Submit / Login Check
    @PostMapping("login")
    public String loginCheck(@ModelAttribute("user") LoginDto user, RedirectAttributes redirectAttributes, HttpSession session) {
        String response = authenticationService.login(user, session);

        // Check for errors
        if (response.startsWith("Error:")) {
            // Pass the error message as a flash attribute
            redirectAttributes.addFlashAttribute("message", response);
            // Redirect back to the sign-up page
            return "redirect:login";
        }

        return "redirect:menu";
    }

    // Menu Section
    @RequestMapping("/menu")
    public ModelAndView getMenuPage(HttpSession session) {
        // Check if user exist.
        ModelAndView view = getModelAndView(session);
        if (view != null) return view;
        return new ModelAndView("Menu");
    }

    // Log Out Section
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        authenticationService.logout(session);
        // Redirect to the login page
        return "redirect:/login";
    }

    // Journey Booking Section
    @RequestMapping("/train-booking")
    public ModelAndView LogJourneyPage(HttpSession session) {
        ModelAndView view = getModelAndView(session);
        if (view != null) return view;
        session.setAttribute("swipeInTime", LocalDateTime.now());
        return new ModelAndView("BookJourney", "stations", new Station());
    }

    // Check For Submit / Journey Check
    @PostMapping("/train-booking")
    public String loginCheck(@ModelAttribute("stations") Station station, RedirectAttributes redirectAttributes, HttpSession session) {

        UserDto user = (UserDto) session.getAttribute("user");
        session.setAttribute("swipeOutTime", LocalDateTime.now());
        List<String> stationList = getStations();

        double feeAmount = abs(stationList.indexOf(station.getEndStation()) - stationList.indexOf(station.getStartStation())) * 5;
        double currentBalance = user.getBalance();

        if (currentBalance < feeAmount) {
            // Pass the error message as a flash attribute
            redirectAttributes.addFlashAttribute("message", "Balance is Insufficient!");
            // Redirect back to the sign-up page
            return "redirect:train-booking";
        }

        TransactionFareDto journey = new TransactionFareDto(
                station.getStartStation(),
                station.getEndStation(),
                (LocalDateTime) session.getAttribute("swipeInTime"),
                (LocalDateTime) session.getAttribute("swipeOutTime"),
                (LocalDateTime) session.getAttribute("swipeOutTime")
        );

        Double newBalance = userService.addTransaction(journey);
        user.setBalance(newBalance);
        session.setAttribute("user", user);


        return "redirect:menu";
    }

    // Add Funds Section
    @RequestMapping("/add-funds")
    public ModelAndView addFundsPage(HttpSession session) {
        ModelAndView view = getModelAndView(session);
        if (view != null) return view;
        return new ModelAndView("AddFunds");
    }

    // Check For Submit / Add Funds Check
    @PostMapping("/addFundUser")
    public String addFundsCheck(@RequestParam("funds") double funds, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");

        TransactionTopUpDto journey = new TransactionTopUpDto(
                funds,
                LocalDateTime.now()
        );
        double newBalance = userService.addBalance(journey);
        user.setBalance(newBalance);
        session.setAttribute("user", user);

        return "redirect:menu";
    }

    // Transactions History Section
    @RequestMapping("/transactions-history")
    public ModelAndView getAllTransactions(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "ALL") String transactionType,
                                           HttpSession session) {
        ModelAndView view = getModelAndView(session);
        if (view != null) return view;
        ModelAndView modelAndView = new ModelAndView();

        List<TransactionDto> transactionList = userService.getTransactions();

        // Save transactions to the session
        session.setAttribute("transaction", transactionList);


        // Pagination setup
        int pageSize = 10; // Define your page size
        int totalPages = (int) Math.ceil((double) transactionList.size() / pageSize);

        // Adjust list to only show items for the current page
        List<TransactionDto> transactionsToShow = getTransactionsDto(page, transactionList, pageSize);

        modelAndView.addObject("transaction", transactionsToShow);
        modelAndView.addObject("transactionType", transactionType);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.setViewName("ShowTransactions");

        return modelAndView;
    }

    // Filtered Transactions History Section
    @GetMapping("/transactions")
    public String getFilteredTransactions(
            @RequestParam(required = false) String transactionType,
            @RequestParam(defaultValue = "0") int page, // Default page to 0
            Model model,
            HttpSession session) {

        // Fetch transactions for the user
        List<TransactionDto> allTransactions = (List<TransactionDto>) session.getAttribute("transaction");

        // Filter transactions based on the selected type if provided
        List<TransactionDto> filteredTransactions = allTransactions;
        if (transactionType != null && !transactionType.equals("ALL")) {
            filteredTransactions = allTransactions.stream()
                    .filter(transaction -> transactionType.equalsIgnoreCase(transaction.getTransactionType().toString()))
                    .toList();
        }

        // Calculate total pages and apply pagination
        int pageSize = 10;
        int totalTransactions = filteredTransactions.size();
        int totalPages = (int) Math.ceil((double) totalTransactions / pageSize);

        // Get transactions for the current page
        List<TransactionDto> transactionsToShow = getTransactionsDto(page, filteredTransactions, pageSize);

        // Add the transactions and selected filter to the model
        model.addAttribute("transaction", transactionsToShow);
        model.addAttribute("transactionType", transactionType);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "ShowTransactions"; // Your Thymeleaf template name
    }


}
