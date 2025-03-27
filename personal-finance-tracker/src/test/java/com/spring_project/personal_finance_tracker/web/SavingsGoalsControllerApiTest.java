package com.spring_project.personal_finance_tracker.web;

import com.spring_project.savingsGoal.service.SavingsGoalService;
import com.spring_project.security.AuthenticationData;
import com.spring_project.user.model.Role;
import com.spring_project.user.service.UserService;
import com.spring_project.web.SavingsGoalsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static com.spring_project.personal_finance_tracker.TestBuilder.getUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

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

        AuthenticationData principal = new AuthenticationData(UUID.randomUUID(), "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = get("/savings/new")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-new-goal"))
                .andExpect(model().attributeExists("createNewGoal"));

    }
}
