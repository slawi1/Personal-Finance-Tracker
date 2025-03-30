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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public String createRecurringPayment(@Valid RecurringPaymentRequest request, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {

        if (bindingResult.hasErrors()) {
            return "recurring-payments";
        }
        User user = userService.getById(authenticationData.getId());
        recurringPaymentService.createRecurringPayment(request, user);

        return "redirect:/recurring/payment";
    }
}
