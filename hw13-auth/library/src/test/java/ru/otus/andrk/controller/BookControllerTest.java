package ru.otus.andrk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.otus.andrk.config.jwt.JwtAuthConverter;
import ru.otus.andrk.config.security.CorsConfig;
import ru.otus.andrk.config.security.SecurityConfig;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoConverterImpl;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.converter.ExceptionToStringMapperImpl;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.BookRepository;
import ru.otus.andrk.service.auth.RoleService;
import ru.otus.andrk.service.data.AuthorService;
import ru.otus.andrk.service.data.BookService;
import ru.otus.andrk.service.data.BookServiceImpl;
import ru.otus.andrk.service.data.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@ContextConfiguration(classes = {BookController.class})
@Import({SecurityConfig.class, BookServiceImpl.class})
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private BookService bookService;

    @MockBean(name = "roleService")
    private RoleService roleService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private JwtAuthConverter jwtAuthConverter;

    @MockBean
    private CorsConfig corsConfig;

    @SpyBean
    private ApiErrorDtoConverterImpl apiErrorDtoConverter;

    @SpyBean
    private DtoMapper dtoMapper;

    @SpyBean
    private ExceptionToStringMapperImpl exceptionToStringMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @ParameterizedTest
    @CsvSource({"/api/v1/book,get", "/api/v1/book,post", "/api/v1/book/999,put", "/api/v1/book/999,delete"})
    public void shouldReturnUnauthorizedForNoAuthenticatedUser(String url, String method) throws Exception {
        var request = makeRequest(url, method).with(csrf()).with(anonymous());
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource({"/api/v1/book,get", "/api/v1/book,post", "/api/v1/book/999,put", "/api/v1/book/999,delete"})
    public void shouldReturnExpectedResultForNoAuthenticatedUser(String url, String method) throws Exception {
        var request = makeRequest(url, method).with(csrf()).with(anonymous());
        var responseString = mockMvc.perform(request).andReturn().getResponse().getContentAsString();
        assertThatNoException().isThrownBy(() -> {
            var actualResult = objectMapper.readValue(responseString, ApiErrorDto.class);
            assertThat(actualResult).extracting("status").isEqualTo(401);
        });
    }

    @ParameterizedTest
    @CsvSource({"/api/v1/book,get", "/api/v1/book,post", "/api/v1/book/999,put", "/api/v1/book/999,delete"})
    public void shouldDontCallBookServiceForNoAuthenticatedUser(String url, String method) throws Exception {
        var request = makeRequest(url, method).with(csrf()).with(anonymous());
        mockMvc.perform(request).andReturn();
        verify(bookService, times(0)).getAllBooks();
        verify(bookService, times(0)).getBookById(anyLong());
        verify(bookService, times(0)).addBook(any());
        verify(bookService, times(0)).modifyBook(anyLong(), any());
        verify(bookService, times(0)).deleteBook(anyLong());
    }


    @ParameterizedTest
    @ValueSource(strings = {"userOne", "userTwo"})
    public void shouldReturnOkResultForAuthenticatedUsersWhenGetList(String user) throws Exception {
        var request = makeRequest("/api/v1/book", "get").with(csrf())
                .with(makeAuthInfo(user));
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user_one", "user_two"})
    public void shouldReturnExpectedDataFromBookServiceForAuthenticatedUsersWhenGetList(String user)
            throws Exception {
        var srcData = getBookList();
        when(bookRepository.findAll()).thenReturn(srcData);
        var request = makeRequest("/api/v1/book", "get").with(csrf())
                .with(makeAuthInfo(user));
        var actualResult = mockMvc.perform(request).andReturn().getResponse().getContentAsString();
        var expectedResult = objectMapper.writeValueAsString(srcData.stream().map(dtoMapper::toDto));
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(bookService, times(1)).getAllBooks();
    }

    @ParameterizedTest
    @CsvSource({"/api/v1/book,post", "/api/v1/book/999,put", "/api/v1/book/999,delete"})
    public void shouldReturnForbiddenForUsersWithoutNeededRoleWhenCallModifyDataEndPoint(
            String url, String method) throws Exception {
        when(roleService.getRolesForAction(anyString())).thenReturn(List.of("valid_role"));
        var request = makeRequest(url, method).with(csrf())
                .with(makeAuthInfo("another_user", "invalid_role"));
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource({"/api/v1/book,post", "/api/v1/book/999,put", "/api/v1/book/999,delete"})
    public void shouldReturnExpectedResultForUsersWithoutNeededRoleWhenCallModifyDataEndPoint(
            String url, String method) throws Exception {
        when(roleService.getRolesForAction(anyString())).thenReturn(List.of("valid_role"));
        var request = makeRequest(url, method).with(csrf())
                .with(makeAuthInfo("another_user", "invalid_role"));
        var responseString = mockMvc.perform(request).andReturn().getResponse().getContentAsString();
        assertThatNoException().isThrownBy(() -> {
            var actualResult = objectMapper.readValue(responseString, ApiErrorDto.class);
            assertThat(actualResult).extracting("status").isEqualTo(403);
        });
    }

    @ParameterizedTest
    @CsvSource({"/api/v1/book,post", "/api/v1/book/999,put", "/api/v1/book/999,delete"})
    public void shouldDontCallBookServiceForUsersWithoutNeededRoleWhenCallModifyDataEndPoint(
            String url, String method) throws Exception {
        when(roleService.getRolesForAction(anyString())).thenReturn(List.of("valid_role"));
        var request = makeRequest(url, method).with(csrf())
                .with(makeAuthInfo("another_user", "invalid_role"));
        mockMvc.perform(request).andReturn();
        verify(bookService, times(0)).addBook(any());
        verify(bookService, times(0)).modifyBook(anyLong(), any());
        verify(bookService, times(0)).deleteBook(anyLong());
    }


    @Test
    public void shouldCallBookServiceAndReturnExpectedDataForAuthenticatedUserWithNeededRoleWhenAddBook()
            throws Exception {
        var srcData = getBookList().get(0);
        when(bookRepository.save(any())).thenReturn(srcData);
        when(roleService.getRolesForAction(anyString())).thenReturn(List.of("valid_role"));

        var request = makeRequest("/api/v1/book", "post", srcData).with(csrf())
                .with(makeAuthInfo("user_login", "invalid_role", "valid_role"));
        var actualResult = mockMvc.perform(request)
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var expectedResult = objectMapper.writeValueAsString(dtoMapper.toDto(srcData));
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(bookService, times(1)).addBook(dtoMapper.toDto(srcData));
    }

    @Test
    public void shouldCallBookServiceAndReturnExpectedDataForAuthenticatedUserWithNeededRoleWhenModifyBook()
            throws Exception {
        var srcData = getBookList().get(0);
        var srcDto = dtoMapper.toDto(srcData);
        when(bookRepository.save(any())).thenReturn(srcData);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(srcData));
        when(roleService.getRolesForAction(anyString())).thenReturn(List.of("valid_role"));

        var request = makeRequest("/api/v1/book/" + srcData.getId(), "put", srcData)
                .with(csrf())
                .with(makeAuthInfo("user_login", "invalid_role", "valid_role"));
        var actualResult = mockMvc.perform(request)
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var expectedResult = objectMapper.writeValueAsString(dtoMapper.toDto(srcData));
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(bookService, times(1)).modifyBook(srcData.getId(), srcDto);
    }

    @Test
    public void shouldCallBookServiceAndReturnExpectedDataForAuthenticatedUserWithNeededRoleWhenDeleteBook()
            throws Exception {
        var exceptedBookId = "88";
        when(roleService.getRolesForAction(anyString())).thenReturn(List.of("valid_role"));

        var request = makeRequest("/api/v1/book/" + exceptedBookId, "delete")
                .with(csrf())
                .with(makeAuthInfo("user_login", "invalid_role", "valid_role"));
        var actualResult = mockMvc.perform(request)
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(exceptedBookId);
        verify(bookService, times(1)).deleteBook(Long.parseLong(exceptedBookId));
    }


    private MockHttpServletRequestBuilder makeRequest(String url, String method, Book data) {
        var json = getBookDtoJson(data);
        return switch (method) {
            case "post" -> MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json);
            case "put" -> MockMvcRequestBuilders.put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json);
            case "delete" -> MockMvcRequestBuilders.delete(url);
            default -> MockMvcRequestBuilders.get(url);
        };
    }

    private MockHttpServletRequestBuilder makeRequest(String url, String method) {
        return makeRequest(url, method, getBookList().get(0));
    }

    private RequestPostProcessor makeAuthInfo(String user, String... roles) {
        var ret = SecurityMockMvcRequestPostProcessors.user(user);
        if (roles.length > 0) {
            ret.roles(roles);
        }
        return ret;
    }

    private String getBookDtoJson(Book book) {
        var data = dtoMapper.toDto(book);
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBookDtoJson() {
        return getBookDtoJson(getBookList().get(0));
    }

    private List<Book> getBookList() {
        return List.of(
                        BookDto.builder().id(1L).name("Book 1").authorId(55L).authorName("Ivanov").build(),
                        BookDto.builder().id(2L).name("Book 2").genreId(55L).genreName("book").build()).stream()
                .map(dto -> new Book(dto.getId(), dto.getName(),
                        (dto.getAuthorId() == null ? null : new Author(dto.getAuthorId(), dto.getAuthorName())),
                        (dto.getGenreId() == null ? null : new Genre(dto.getGenreId(), dto.getGenreName())),
                        new ArrayList<>()))
                .toList();
    }

}