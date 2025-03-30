package app.web;

import app.exception.GoalNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        return new ModelAndView("404-not-found");
    }

    @ExceptionHandler(GoalNotFoundException.class)
    public ModelAndView handleException2(Exception ex) {
        return new ModelAndView("savings-goals");
    }
}
