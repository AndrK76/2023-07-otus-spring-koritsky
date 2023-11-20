package ru.otus.andrk.service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.config.SecurityConfig;
import ru.otus.andrk.controller.BookController;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.service.auth.UserService;
import ru.otus.andrk.service.library.AuthorService;
import ru.otus.andrk.service.library.BookService;
import ru.otus.andrk.service.library.GenreService;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@ContextConfiguration(classes = {BookController.class})
@Import(SecurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void initMockServices() {
        var testBook = getTestBook();
        given(bookService.getBookById(anyLong())).willReturn(testBook);
    }

    @ParameterizedTest
    @MethodSource("getControllerUrls")
    void shouldRedirectToLoginForNonAuthorizedUser_AllGetMethods(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithMockUser(username = "validUser")
    @ParameterizedTest
    @MethodSource("getControllerUrls")
    void shouldReturnOkForAuthorizedUser_AllGetMethods(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDontAddBookAndReturnRedirectLoginForNonAuthorizedUserWhenPostBookWithCsrf() throws Exception {
        mockMvc.perform(post("/book/add").flashAttr("book", getTestBook()).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
        verify(bookService, times(0))
                .addBook(any(), any(), any());
    }

    @Test
    void shouldDontAddBookAndReturnForbiddenForNonAuthorizedUserWhenPostBookWithoutCsrf() throws Exception {
        mockMvc.perform(post("/book/add").flashAttr("book", getTestBook()))
                .andExpect(status().isForbidden());
        verify(bookService, times(0))
                .addBook(any(), any(), any());
    }

    @WithMockUser(username = "validUser")
    @Test
    void shouldAddBookAndReturnRedirectOnBookListForAuthorizedUserWhenPostBookWithCsrf() throws Exception {
        mockMvc.perform(post("/book/add").flashAttr("book", getTestBook()).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1))
                .addBook(any(), any(), any());
    }

    @WithMockUser(username = "validUser")
    @Test
    void shouldDontAddBookAndForbiddenForAuthorizedUserWhenPostBookWithoutCsrf() throws Exception {
        mockMvc.perform(post("/book/add").flashAttr("book", getTestBook()))
                .andExpect(status().isForbidden());
        verify(bookService, times(0))
                .addBook(any(), any(), any());
    }

    @Test
    void shouldDontEditBookAndReturnRedirectToLoginForNonAuthorizedUserWhenPostBook() throws Exception {
        mockMvc.perform(post("/book/edit").flashAttr("book", getTestBook()).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
        verify(bookService, times(0))
                .modifyBook(anyLong(), any(), any(), any());
    }

    @WithMockUser(username = "validUser")
    @Test
    void shouldEditBookAndReturnRedirectForAuthorizedUserWhenPostBook() throws Exception {
        mockMvc.perform(post("/book/edit").flashAttr("book", getTestBook()).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1))
                .modifyBook(anyLong(), any(), any(), any());
    }

    @Test
    void shouldDontDeleteBookAndRedirectToLoginForNonAuthorizedUserWhenPostBook() throws Exception {
        mockMvc.perform(post("/book/delete").param("id", "1").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
        verify(bookService, times(0))
                .deleteBook(anyLong());
    }

    @WithMockUser(username = "validUser")
    @Test
    void shouldDeleteBookAndReturnRedirectForAuthorizedUserWhenPostBook() throws Exception {
        mockMvc.perform(post("/book/delete").param("id", "1").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1))
                .deleteBook(1L);
    }


    private static Stream<String> getControllerUrls() {
        return Stream.of(
                "/book", "/book/add", "/book/edit?id=1", "/book/delete?id=1"
        );
    }

    private BookDto getTestBook() {
        return BookDto.builder().name("name").build();
    }


}
