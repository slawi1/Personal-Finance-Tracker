package com.spring_project.web;

import com.spring_project.category.service.CategoryService;
import com.spring_project.security.AuthenticationData;
import com.spring_project.user.model.User;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.AddCategoryRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/add-category")
    public ModelAndView addCategory() {

        ModelAndView modelAndView = new ModelAndView("add-category");
        modelAndView.addObject("addCategoryName", new AddCategoryRequest());

        return modelAndView;
    }
    @PostMapping("/add-category")
    public ModelAndView addNewCategory(@Valid AddCategoryRequest addCategoryRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationData authenticationData) {


        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-category");
        }

        User user = userService.findByUsername(authenticationData.getUsername());
        categoryService.addCategory(addCategoryRequest.getCategoryName(), user, BigDecimal.valueOf(0));

        return new ModelAndView("redirect:/home");
    }
}
