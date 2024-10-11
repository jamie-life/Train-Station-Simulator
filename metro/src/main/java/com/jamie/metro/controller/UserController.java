package com.jamie.metro.controller;

import com.jamie.metro.dto.*;
import com.jamie.metro.entity.Station;
import com.jamie.metro.entity.Transaction;
import com.jamie.metro.entity.TransactionType;
import com.jamie.metro.entity.User;
import com.jamie.metro.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.abs;

@AllArgsConstructor
@Controller
public class UserController {

    private AuthenticationService authenticationService;

    // General Section
    @ModelAttribute("stationNames") // Station List is stored locally, just for testing.
    List<String> getStations(){
        return Arrays.asList("Station 1", "Station 2", "Station 3", "Station 4");
    }

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
        return new ModelAndView("User/login", "user", new LoginDto());
    }

    // Check For Submit / Login Check
    @PostMapping("login")
    public String loginCheck(@ModelAttribute("user") LoginDto user, RedirectAttributes redirectAttributes, HttpSession session) {
        String response = authenticationService.login(user);
        System.out.println(response);

        // Check for errors
        if (response.startsWith("Error:")) {
            // Pass the error message as a flash attribute
            redirectAttributes.addFlashAttribute("message", response);
            // Redirect back to the sign-up page
            return "redirect:login";
        }

        UserDto saveUser = authenticationService.getUser(user.getUsername()).getBody();
        session.setAttribute("user", saveUser);
        return "redirect:menu";
    }

    // Menu Section
    @RequestMapping("/menu")
    public ModelAndView getMenuPage() {
        return new ModelAndView("Menu");
    }

    // Journey Booking Section
    @RequestMapping("/train-booking")
    public ModelAndView LogJourneyPage(HttpSession session) {
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
                user.getId(),
                station.getStartStation(),
                station.getEndStation(),
                (LocalDateTime) session.getAttribute("swipeInTime"),
                (LocalDateTime) session.getAttribute("swipeOutTime"),
                (LocalDateTime) session.getAttribute("swipeOutTime")
        );

        Double newBalance = authenticationService.addTransaction(journey);
        user.setBalance(newBalance);
        session.setAttribute("user", user);


        return "redirect:menu";
    }

    // Add Funds Section
    @RequestMapping("/add-funds")
    public ModelAndView addFundsPage() {
        return new ModelAndView("AddFunds");
    }

    // Check For Submit / Sign Up Check
    @PostMapping("/addFundUser")
    public String addFundsCheck(@RequestParam("funds") double funds, Model model, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");

        if (user != null) {
            // Access the user's ID
            TransactionTopUpDto journey = new TransactionTopUpDto(
                    user.getId(),
                    funds,
                    LocalDateTime.now()
            );
            double newBalance = authenticationService.addBalance(journey);
            user.setBalance(newBalance);
            session.setAttribute("user", user);

        } else {
            System.out.println("No user found in session");
        }
        return "/AddFunds";
    }

    @RequestMapping("/transactions-history")
    public ModelAndView showAllEmployeesController(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        User user = (User)session.getAttribute("user");


        Collection<Transaction> transactionList = new ArrayList<>();
        transactionList.add(new Transaction(
                1L,
                user.getUserId(),
                TransactionType.FARE,
                "Station 1",
                "Station 2",
                LocalDateTime.now(),
                LocalDateTime.now(),
                5.0,
                10.0,
                5.0,
                LocalDateTime.now()
                ));


        modelAndView.addObject("transactions", transactionList);
        modelAndView.setViewName("ShowTransactions");

        return modelAndView;
    }
}
