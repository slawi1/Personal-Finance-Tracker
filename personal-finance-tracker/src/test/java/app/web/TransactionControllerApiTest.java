package app.web;

import app.security.AuthenticationData;
import app.transaction.model.Transaction;
import app.transaction.service.TransactionService;
import app.user.model.Role;
import app.user.model.User;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static app.TestBuilder.getUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(TransactionController.class)
public class TransactionControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToTransactions_shouldReturnTransactionsPage() throws Exception {

        when(userService.getById(any())).thenReturn(getUser());
        UUID userId = UUID.randomUUID();
        User user = getUser();
        List<Transaction> transactions = user.getTransactions();


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = get("/transactions")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("transactions"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("transactions"));
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void getRequestToAddCashToUser_shouldReturnAddCashPage() throws Exception {

        when(userService.getById(any())).thenReturn(getUser());
        UUID userId = UUID.randomUUID();


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = get("/transactions/add/cash")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("transactions-add-cash"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("addCashRequest"));
        verify(userService, times(1)).getById(userId);
    }

}