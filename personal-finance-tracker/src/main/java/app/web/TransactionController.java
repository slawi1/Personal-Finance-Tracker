package app.web;

import app.security.AuthenticationData;
import app.transaction.model.Transaction;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AddCashRequest;
import jakarta.validation.Valid;
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
        List<Transaction> sorted = user.getTransactions().stream().sorted(Comparator.comparing(Transaction::getTransactionCreationDate).reversed()).toList();
        ModelAndView modelAndView = new ModelAndView("transactions");
        modelAndView.addObject("user", user);
        modelAndView.addObject("transactions", sorted);
        return modelAndView;

    }

    @GetMapping("/add/cash")
    public ModelAndView addCashPage(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        ModelAndView modelAndView = new ModelAndView("transactions-add-cash");
        modelAndView.addObject("user", user);
        modelAndView.addObject("addCashRequest", new AddCashRequest());

        return modelAndView;

    }

    @PostMapping("/add/cash")
    public ModelAndView addCashToUser(@Valid AddCashRequest addCashRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {
        User user = userService.getById(authenticationData.getId());

        ModelAndView modelAndView = new ModelAndView("transactions-add-cash");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("user", user);
            modelAndView.addObject("addCashRequest", addCashRequest);
            modelAndView.addObject("bindingResult", bindingResult);
            return modelAndView;
        }

        transactionService.addCashTransaction(addCashRequest, user, addCashRequest.getCategory());

        return new ModelAndView("redirect:/home");

    }

}
