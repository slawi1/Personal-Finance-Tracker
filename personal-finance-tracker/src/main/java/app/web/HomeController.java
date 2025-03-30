package app.web;

import app.category.service.CategoryService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;


    public HomeController(UserService userService, TransactionService transactionService, CategoryService categoryService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
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

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        userService.register(registerRequest);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        List<Transaction> sorted = user.getTransactions().stream().sorted(Comparator.comparing(Transaction::getTransactionDate).reversed()).limit(10).toList();
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("addExpenseRequest", new AddExpenseRequest());
        modelAndView.addObject("transactions", sorted);
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addExpense(@Valid AddExpenseRequest addExpenseRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("users");
        }
        User user = userService.getById(authenticationData.getId());
        transactionService.createExpenseTransaction(addExpenseRequest, user, addExpenseRequest.getCategory());


        return new ModelAndView("redirect:/home");

    }

    @DeleteMapping("/home/{id}")
    public String deleteCategory(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationData authenticationData) {
        categoryService.deleteCategory(id);
        return "redirect:/home";

    }

    @PutMapping("/home/{id}")
    public String restoreDeletedCategory(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationData authenticationData) {
        categoryService.restoreDeletedCategory(id);
        return "redirect:/category/add";
    }

}

