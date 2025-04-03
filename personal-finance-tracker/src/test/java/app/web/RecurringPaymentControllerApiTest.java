package app.web;

import app.recurringPayment.service.RecurringPaymentService;
import app.security.AuthenticationData;
import app.user.model.Role;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(RecurringPaymentController.class)
public class RecurringPaymentControllerApiTest {

    @MockitoBean
    private RecurringPaymentService recurringPaymentService;
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToRecurringPayment_shouldReturnRecurringPaymentPage() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());

        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = get("/recurring/payment")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("recurring-payments"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("recurringPaymentRequest"));

        verify(userService, times(1)).getById(userId);
    }

    @Test
    void postRequestToCreateRecurringPayment_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = post("/recurring/payment/add")
                .formField("paymentName", "Test Payment")
                .formField("amount", "100")
                .formField("paymentDate", LocalDate.now().toString())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recurring/payment"));

        verify(recurringPaymentService, times(1)).createRecurringPayment(any(), any());
    }

    @Test
    void postRequestToCreateRecurringPaymentWithInvalidData_shouldReturnModelAndView() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = post("/recurring/payment/add")
                .formField("paymentName", "Te")
                .formField("amount", "-100")
                .formField("paymentDate", LocalDate.now().toString())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("recurring-payments"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("recurringPaymentRequest"))
                .andExpect(model().attributeExists("bindingResult"));

        verify(recurringPaymentService, never()).createRecurringPayment(any(), any());
    }

}
