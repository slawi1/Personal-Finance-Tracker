package app.web;

import app.savingsGoal.service.SavingsGoalService;
import app.security.AuthenticationData;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AddCashToGoalRequest;
import app.web.dto.CreateNewGoalRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/savings")
public class SavingsGoalsController {

    private final UserService userService;
    private final SavingsGoalService savingsGoalService;

    public SavingsGoalsController(UserService userService, SavingsGoalService savingsGoalService) {
        this.userService = userService;
        this.savingsGoalService = savingsGoalService;
    }

    @GetMapping
    public ModelAndView showGoals(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        ModelAndView modelAndView = new ModelAndView("savings-goals");
        modelAndView.addObject("user", user);
        modelAndView.addObject("addCashToGoalRequest", new AddCashToGoalRequest());
        return modelAndView;
    }
    @GetMapping("/new")
    private ModelAndView addNewGoals() {
        ModelAndView modelAndView = new ModelAndView("add-new-goal");
        modelAndView.addObject("createNewGoal", new CreateNewGoalRequest());
        return modelAndView;
    }

    @PostMapping("/new")
    public String createNewGoal(@Valid CreateNewGoalRequest request, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {

        if (bindingResult.hasErrors()) {
            return "add-new-goal";
        }
        User user = userService.getById(authenticationData.getId());
        savingsGoalService.createNewGoal(request, user);
        return "redirect:/savings";
    }


    @PostMapping("/add/cash")
    public ModelAndView addCash(@Valid AddCashToGoalRequest request, @AuthenticationPrincipal AuthenticationData authenticationData) {


        User user = userService.getById(authenticationData.getId());
        savingsGoalService.addCashToGoal(request, user);

        return new ModelAndView("redirect:/savings");
    }
}
