package app.web;

import app.security.AuthenticationData;
import app.transaction.model.Transaction;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AddExpenseRequest;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

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
    public ModelAndView getLoginPage(@RequestParam (value = "error", required = false) String errorParam) {

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        if (errorParam != null) {
            modelAndView.addObject("error", "Incorrect username or password.");
        }

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

        ModelAndView modelAndView = new ModelAndView("register");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("bindingResult", bindingResult);
            modelAndView.addObject("registerRequest", registerRequest);
            return modelAndView;
        }
        userService.register(registerRequest);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        List<Transaction> sorted = user.getTransactions().stream().sorted(Comparator.comparing(Transaction::getTransactionCreationDate).reversed()).limit(10).toList();
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("addExpenseRequest", new AddExpenseRequest());
        modelAndView.addObject("transactions", sorted);
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addExpense(@Valid AddExpenseRequest addExpenseRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {
        User user = userService.getById(authenticationData.getId());
        List<Transaction> sorted = user.getTransactions().stream().sorted(Comparator.comparing(Transaction::getTransactionCreationDate).reversed()).limit(10).toList();
        ModelAndView modelAndView = new ModelAndView("home");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("user", user);
            modelAndView.addObject("bindingResult", bindingResult);
            modelAndView.addObject("addExpenseRequest", addExpenseRequest);
            modelAndView.addObject("transactions", sorted);
            return modelAndView;
        }

        transactionService.createExpenseTransaction(addExpenseRequest, user, addExpenseRequest.getCategory());
        return new ModelAndView("redirect:/home");

    }

}

