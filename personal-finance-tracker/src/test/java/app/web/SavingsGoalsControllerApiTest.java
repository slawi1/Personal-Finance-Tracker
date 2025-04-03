package app.web;

import app.savingsGoal.service.SavingsGoalService;
import app.security.AuthenticationData;
import app.user.model.Role;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.UUID;

import static app.TestBuilder.getUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SavingsGoalsController.class)
public class SavingsGoalsControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private SavingsGoalService savingsGoalService;


    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToSavings_shouldReturnSavingsGoalsPage() throws Exception {

        when(userService.getById(any())).thenReturn(getUser());
        UUID userId = UUID.randomUUID();

        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = get("/savings")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("savings-goals"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("addCashToGoalRequest"));
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void getRequestToAddNewSavingsGoal_shouldReturnAddNewSavingsGoalsPage() throws Exception {

        when(userService.getById(any())).thenReturn(getUser());
        UUID userId = UUID.randomUUID();

        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = get("/savings/new")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-new-goal"))
                .andExpect(model().attributeExists("createNewGoal"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    void postRequestToSavingsGoals_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = post("/savings/new")
                .formField("goalName", "Test Payment")
                .formField("targetAmount", "100")
                .formField("deadline", LocalDate.now().toString())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/savings"));

        verify(savingsGoalService, times(1)).createNewGoal(any(), any());
    }

    @Test
    void postRequestToSavingsGoalsWithInvalidData_shouldReturnModelAndView() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = post("/savings/new")
                .formField("goalName", "Te")
                .formField("targetAmount", "-100")
                .formField("deadline", LocalDate.now().toString())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-new-goal"))
                .andExpect(model().attributeExists("createNewGoal"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("bindingResult"));

        verify(savingsGoalService, never()).createNewGoal(any(), any());
    }


    @Test
    void postRequestToAddCashToGoal_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = post("/savings/add/cash")
                .formField("amount", "100")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/savings"));

        verify(savingsGoalService, times(1)).addCashToGoal(any(), any());
    }

    @Test
    void postRequestToAddCashToGoalWithInvalidDate_shouldReturnModelAndView() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = post("/savings/add/cash")
                .formField("amount", "-100")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("savings-goals"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("addCashToGoalRequest"))
                .andExpect(model().attributeExists("bindingResult"));

        verify(savingsGoalService, never()).addCashToGoal(any(), any());
    }



}
