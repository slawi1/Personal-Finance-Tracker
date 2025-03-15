package com.spring_project.web;

import com.spring_project.security.AuthenticationData;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.transaction.service.TransactionService;
import com.spring_project.user.model.User;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.AddExpenseRequest;
import com.spring_project.web.dto.AddTransactionRequest;
import com.spring_project.web.dto.LoginRequest;
import com.spring_project.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class HomeController {

    private final UserService userService;
    private final TransactionService transactionService;


    public HomeController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String getIndexPage() {

        return "index";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        userService.register(registerRequest);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("addExpenseRequest", new AddExpenseRequest());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addExpense(@Valid AddExpenseRequest addExpenseRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("/transactions-add");
        }
        User user = userService.getById(authenticationData.getId());
        UUID categoryId = addExpenseRequest.getCategory();
        Transaction transaction = transactionService.createExpenseTransaction(addExpenseRequest, user, categoryId);


        return new ModelAndView("redirect:/home");

    }


}

