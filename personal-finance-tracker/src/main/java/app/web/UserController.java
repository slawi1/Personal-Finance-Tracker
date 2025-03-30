package app.web;

import app.security.AuthenticationData;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.EditProfileRequest;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/edit")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/profile")
    public ModelAndView editProfile(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        ModelAndView modelAndView = new ModelAndView("edit-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("editProfileRequest", DtoMapper.mapUserToEditProfileRequest(user));
        return modelAndView;
    }

    @PutMapping("/profile/{id}")
    public ModelAndView editProfile(@PathVariable UUID id, @Valid EditProfileRequest editProfileRequest, BindingResult bindingResult) {

        User user = userService.getById(id);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("edit-profile");
        }
        userService.editProfile(id, editProfileRequest);

        return new ModelAndView("redirect:/home");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ModelAndView users() {
        List<User> allUsers = userService.allUsers();
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("users", allUsers);
        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("users/{id}/status")
    public String switchStatus(@PathVariable UUID id) {
        userService.switchStatus(id);
        return "redirect:/edit/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("users/{id}/role")
    public String changeRole(@PathVariable UUID id) {
        userService.changeRole(id);
        return "redirect:/edit/users";
    }
}
