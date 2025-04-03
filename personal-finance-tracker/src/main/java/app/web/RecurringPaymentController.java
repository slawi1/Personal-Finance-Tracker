package app.web;

import app.recurringPayment.service.RecurringPaymentService;
import app.security.AuthenticationData;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.RecurringPaymentRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/recurring/payment")
public class RecurringPaymentController {

    private final RecurringPaymentService recurringPaymentService;
    private final UserService userService;


    public RecurringPaymentController(RecurringPaymentService recurringPaymentService, UserService userService) {
        this.recurringPaymentService = recurringPaymentService;
        this.userService = userService;

    }

    @GetMapping
    public ModelAndView getRecurringPaymentPage(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        ModelAndView modelAndView = new ModelAndView("recurring-payments");
        modelAndView.addObject("user", user);
        modelAndView.addObject("recurringPaymentRequest", new RecurringPaymentRequest());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView createRecurringPayment(@Valid RecurringPaymentRequest recurringPaymentRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {
        User user = userService.getById(authenticationData.getId());

        ModelAndView modelAndView = new ModelAndView("recurring-payments");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("user", user);
            modelAndView.addObject("recurringPaymentRequest", recurringPaymentRequest);
            modelAndView.addObject("bindingResult", bindingResult);
            return modelAndView;
        }
        recurringPaymentService.createRecurringPayment(recurringPaymentRequest, user);

        return new ModelAndView("redirect:/recurring/payment");
    }


    @PutMapping("/{id}")
    public String changePaymentStatus(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationData authenticationData) {
        recurringPaymentService.changeStatus(id);
        return "redirect:/recurring/payment";
    }
}
