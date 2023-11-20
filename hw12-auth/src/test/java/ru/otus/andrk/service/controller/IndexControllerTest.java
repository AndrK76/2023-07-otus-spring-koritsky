package ru.otus.andrk.service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.config.SecurityConfig;
import ru.otus.andrk.controller.IndexController;
import ru.otus.andrk.service.auth.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IndexController.class)
@ContextConfiguration(classes = {IndexController.class})
@Import(SecurityConfig.class)
public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnOkOnNoAuthorizedUser() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @WithMockUser(username = "validUser")
    @Test
    void shouldReturnRedirectOnAuthorizedUser() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isFound());
    }
}
