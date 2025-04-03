package app.web;

import app.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        return new ModelAndView("internal-server-error").addObject("errorMessage", ex.getClass().getSimpleName());

    }

    @ExceptionHandler(GoalNotFoundException.class)
    public ModelAndView handleException2(Exception ex) {
        return new ModelAndView("savings-goals");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({AccessDeniedException.class
            , NoResourceFoundException.class
            , MethodArgumentTypeMismatchException.class
            , MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundException(Exception ex) {
        return new ModelAndView("404-not-found");

    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public String handleUsernameAlreadyExists(HttpServletRequest request, RedirectAttributes redirectAttributes, UsernameAlreadyExistException ex) {
        redirectAttributes.addFlashAttribute("usernameErrorMessage", ex.getMessage());
        return "redirect:/register";
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public String handleEmailAlreadyRegisteredException(HttpServletRequest request, RedirectAttributes redirectAttributes, EmailAlreadyRegisteredException ex) {
        redirectAttributes.addFlashAttribute("emailErrorMessage", ex.getMessage());
        return "redirect:/register";
    }


    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public String handleCategoryAlreadyExistsException(HttpServletRequest request, RedirectAttributes redirectAttributes, CategoryAlreadyExistsException ex) {
        redirectAttributes.addFlashAttribute("categoryErrorMessage", ex.getMessage());
        return "redirect:/category/add";
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public String handlePasswordsDoNotMatch(HttpServletRequest request, RedirectAttributes redirectAttributes, PasswordsDoNotMatchException ex) {
        redirectAttributes.addFlashAttribute("passwordErrorMessage", ex.getMessage());
        return "redirect:/register";
    }

    @ExceptionHandler(MaximumCategoriesReachedException.class)
    public String handleMaximumCategoriesReached(HttpServletRequest request, RedirectAttributes redirectAttributes, MaximumCategoriesReachedException ex) {
        redirectAttributes.addFlashAttribute("categoryErrorMessage", ex.getMessage());
        return "redirect:/category/add";
    }



}
