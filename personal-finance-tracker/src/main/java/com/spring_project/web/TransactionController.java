package com.spring_project.web;

import com.spring_project.security.AuthenticationData;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.transaction.service.TransactionService;
import com.spring_project.user.model.User;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.AddCashRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final UserService userService;
    private final TransactionService transactionService;


    public TransactionController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;

    }


    @GetMapping
    public ModelAndView showTransactions(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        List<Transaction> sorted = user.getTransactions().stream().sorted(Comparator.comparing(Transaction::getTransactionDate).reversed()).toList();
        ModelAndView modelAndView = new ModelAndView("transactions");
        modelAndView.addObject("user", user);
        modelAndView.addObject("transactions", sorted);
        return modelAndView;

    }

    @GetMapping("/add/cash")
    public ModelAndView addCashPage(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        ModelAndView modelAndView = new ModelAndView("/transactions-add-cash");
        modelAndView.addObject("user", user);
        modelAndView.addObject("addCashRequest", new AddCashRequest());

        return modelAndView;

    }

    @PostMapping("/add/cash")
    public ModelAndView addNewTransaction(AddCashRequest addCashRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("/transactions-add-cash");
        }
        User user = userService.getById(authenticationData.getId());

        transactionService.addCashTransaction(addCashRequest, user, addCashRequest.getCategory());

        return new ModelAndView("redirect:/home");

    }

}
