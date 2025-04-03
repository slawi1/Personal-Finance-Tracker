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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

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

        User user = userService.getById(authenticationData.getId());
        ModelAndView modelAndView = new ModelAndView("add-category");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("user", user);
            modelAndView.addObject("bindingResult", bindingResult);
            modelAndView.addObject("addCategoryName", addCategoryRequest);

            return modelAndView;
        }
        categoryService.addCategory(addCategoryRequest.getCategoryName(), user);

        return new ModelAndView("redirect:/home");
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return "redirect:/home";
    }

    @PutMapping("/{id}")
    public String restoreDeletedCategory(@PathVariable UUID id) {
        categoryService.restoreDeletedCategory(id);
        return "redirect:/category/add";
    }
}
