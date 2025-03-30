package app.web;

import app.category.service.CategoryService;
import app.security.AuthenticationData;
import app.transaction.service.TransactionService;
import app.user.model.Role;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static app.TestBuilder.getUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private TransactionService transactionService;
    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToIndex_shouldReturnIndexPage() throws Exception {

        MockHttpServletRequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

    }

    @Test
    void getRequestToRegister_shouldReturnRegisterPage() throws Exception {

        MockHttpServletRequestBuilder request = get("/register");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }

    @Test
    void postRequestToRegister_happyPath() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "user")
                .formField("email", "user@abv.bg")
                .formField("password", "1234")
                .formField("ConfirmPassword", "1234")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterWithInvalidData_shouldReturnRegisterView() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "")
                .formField("email", "user@abv.bg")
                .formField("password", "1234")
                .formField("ConfirmPassword", "1234")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
        verify(userService, never()).register(any());
    }

    @Test
    void getRequestToLogin_shouldReturnLoginPage() throws Exception {

        MockHttpServletRequestBuilder request = get("/login");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"));
    }

    @Test
    void getRequestToLoginWithErrorParam_shouldReturnLoginPageWithErrorMessage() throws Exception {

        MockHttpServletRequestBuilder request = get("/login").param("error", "");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest", "error"));
    }

    @Test
    void getRequestToHomeWithUnauthenticatedUser_shouldRedirectToLogin() throws Exception {

        MockHttpServletRequestBuilder request = get("/home");
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
        verify(userService, never()).getById(any());
    }


    @Test
    void getRequestToHomeWithAuthenticatedUser_shouldReturnHomePage() throws Exception {

        when(userService.getById(any())).thenReturn(getUser());

        UUID userId = UUID.randomUUID();
        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = get("/home")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("addExpenseRequest"))
                .andExpect(model().attributeExists("transactions"));
        verify(userService, times(1)).getById(userId);
    }
}
