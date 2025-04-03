package app.web;

import app.category.service.CategoryService;
import app.security.AuthenticationData;
import app.user.model.Role;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(CategoryController.class)
public class CategoryControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getRequestToAddCategory_shouldReturnAddCategoryPage() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = get("/category/add")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("addCategoryName"))
                .andExpect(model().attributeExists("categories"));
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void postRequestToAddCategory_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());


        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = post("/category/add")
                .formField("categoryName", "Test Category")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(categoryService, times(1)).addCategory(any(), any());
    }

    @Test
    void postRequestToAddCategory_withInvalidCategoryName_shouldReturnModelAndView() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(getUser());

        AuthenticationData principal = new AuthenticationData(userId, "user123", "1234", Role.USER, true);
        MockHttpServletRequestBuilder request = post("/category/add")
                .formField("categoryName", "Te")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("addCategoryName"))
                .andExpect(model().attributeExists("bindingResult"));


        verify(categoryService, never()).addCategory(any(), any());
    }
}
