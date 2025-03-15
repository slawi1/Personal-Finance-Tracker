package com.spring_project.web;

import com.spring_project.category.model.Category;
import com.spring_project.security.AuthenticationData;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.transaction.service.TransactionService;
import com.spring_project.user.model.User;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.AddTransactionRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final UserService userService;
    private final TransactionService transactionService;

    public TransactionController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;

    }

//    @PostMapping("/new")
//    public ModelAndView addExpense(@Valid AddExpenseRequest addExpenseRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {
//
//        if (bindingResult.hasErrors()) {
//            return new ModelAndView( "/home");
//        }
//
//        User user = userService.findByUsername(authenticationData.getUsername());
//        Transaction transaction = transactionService.createExpenseTransaction(addExpenseRequest, user);
//        ModelAndView modelAndView = new ModelAndView("/home");
//        modelAndView.addObject("user", user);
//        return new ModelAndView("redirect:/home");
//
//    }

/// //////
//    @GetMapping("/add")
//    public ModelAndView getAddNewTransactionPage2(@AuthenticationPrincipal AuthenticationData authenticationData) {
//
//        User user = userService.getById(authenticationData.getId());
//        ModelAndView modelAndView = new ModelAndView("/transactions-add");
//        modelAndView.addObject("user", user);
//        modelAndView.addObject("addTransactionRequest", new AddTransactionRequest());
//        modelAndView.addObject("categories", user.getCategories());
//
//        return modelAndView;
//
//    }
//
//    @PostMapping("/add")
//    public ModelAndView addNewTransaction(AddTransactionRequest addTransactionRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {
//
//        if (bindingResult.hasErrors()) {
//            return new ModelAndView("/transactions-add");
//        }
//        User user = userService.getById(authenticationData.getId());
//        UUID categoryId = addTransactionRequest.getCategory();
//        Transaction transaction = transactionService.createTransaction(addTransactionRequest, user, categoryId);
//
//
//        return new ModelAndView("redirect:/home");
//
//    }


    /// /////
//    @PostMapping("/add")
//    public String addNewTransaction2(@ModelAttribute("addTransactionRequest") AddTransactionRequest addTransactionRequest,
//                                     @AuthenticationPrincipal AuthenticationData authenticationData, Model model) {
//
//        User user = userService.getById(authenticationData.getId());
//        model.addAttribute("user", user);
//        Transaction transaction = transactionService.createTransaction(addTransactionRequest,user);
//
//
//        return "redirect:/home";
//
//    }


}
