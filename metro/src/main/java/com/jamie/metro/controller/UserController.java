package com.jamie.metro.controller;

import com.jamie.metro.dto.LoginDto;
import com.jamie.metro.entity.Station;
import com.jamie.metro.entity.TransactionType;
import com.jamie.metro.entity.Transactions;
import com.jamie.metro.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Controller
public class UserController {

    @ModelAttribute("stationNames")
    List<String> getStations(){
        return Arrays.asList("Test", "Test2", "Test3", "Test4");
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:login";  // Redirects the user to the "/login" page
    }

    // Sign Up Section
    @RequestMapping("signUp")
    public ModelAndView getSignUpPage() {
        return new ModelAndView("User/SignUp", "user", new User());
    }

    // Check For Submit / Sign Up Check
    @RequestMapping(value = "signUp", method = RequestMethod.POST)
    public String registerCheck(@ModelAttribute("user") User user, HttpSession session) {
        System.out.println("Username: " + user.getUsername()
                + ". Password: " + user.getPassword()
                + ". Email: " + user.getEmail()
                + ". First Name: " + user.getFirstName()
                + ". Last Name: " + user.getLastName());

        // Create a new instance here

        // Add logic to check the login credentials, set session attributes, etc.

        return "redirect:login";
    }

    // Login Section
    @RequestMapping("login")
    public ModelAndView LoginPage() {
        return new ModelAndView("User/login", "user", new LoginDto());
    }

    // Check For Submit / Login Check
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginCheck(@ModelAttribute("user") LoginDto user, HttpSession session) {
        System.out.println("Username: " + user.getUsername() + ". Password: " + user.getPassword());

        // Create a new instance here

        // Examples, username, first name and last name would be returned with jwt and balance

        User saveUser = new User(
                1L,
                "Jamie",
                "Grant",
                "Jamie",
                "JamieGrant@outlook.com",
                "Test",
                10


        );

        System.out.println(saveUser);
        session.setAttribute("user", saveUser);

        return "redirect:menu";
    }

    // Menu Section
    @RequestMapping("/menu")
    public ModelAndView getMenuPage() {
        return new ModelAndView("Menu");
    }

    @RequestMapping("/train-booking")
    public ModelAndView LogJourneyPage() {
        return new ModelAndView("BookJourney", "stations", new Station());
    }

    @RequestMapping("/add-funds")
    public ModelAndView addFundsPage() {
        return new ModelAndView("AddFunds");
    }

    @RequestMapping("/transactions-history")
    public ModelAndView showAllEmployeesController(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        User user = (User)session.getAttribute("user");


        Collection<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(new Transactions(
                1L,
                user.getUserId(),
                TransactionType.FARE,
                1,
                2,
                LocalDateTime.now(),
                LocalDateTime.now(),
                5.0,
                10.0,
                5.0,
                LocalDateTime.now()
                ));


        modelAndView.addObject("transactions", transactionsList);
        modelAndView.setViewName("ShowTransactions");

        return modelAndView;
    }
}
