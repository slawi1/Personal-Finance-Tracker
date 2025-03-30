package app.web;

import app.category.service.CategoryService;
import app.security.AuthenticationData;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AddCategoryRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public ModelAndView addCategory(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());
        ModelAndView modelAndView = new ModelAndView("add-category");
        modelAndView.addObject("addCategoryName", new AddCategoryRequest());
        modelAndView.addObject("user", user);
        modelAndView.addObject("categories", user.getCategories());

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addNewCategory(@Valid AddCategoryRequest addCategoryRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {


        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-category");
        }

        User user = userService.getById(authenticationData.getId());
        categoryService.addCategory(addCategoryRequest.getCategoryName(), user);

        return new ModelAndView("redirect:/home");
    }
}
