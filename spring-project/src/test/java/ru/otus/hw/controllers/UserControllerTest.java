package ru.otus.hw.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    void shouldReturnAllUsers() throws Exception {
//        List<UserDto> users = List.of(
//                new UserDto(1L, "john_doe", "john@example.com", "password_hash", new TariffDto(1L, "FREE")),
//                new UserDto(2L, "jane_doe", "jane@example.com", "password_hash", new TariffDto(2L, "BASIC"))
//        );
//        Mockito.when(userService.findAll()).thenReturn(users);
//
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(users.size()))
//                .andExpect(jsonPath("$[0].username").value("john_doe"))
//                .andExpect(jsonPath("$[0].email").value("john@example.com"))
//                .andExpect(jsonPath("$[0].password").value("password_hash"))
//                .andExpect(jsonPath("$[0].user.tariff.name").value("FREE"));
//    }
//
//    @Test
//    void shouldReturnUserById() throws Exception {
//        UserDto book = new UserDto(1L,"john_doe", "john@example.com", "password_hash", new TariffDto(1L, "FREE"));
//        Mockito.when(userService.findById(1L)).thenReturn(book);
//
//        mockMvc.perform(get("/api/users/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].username").value("john_doe"))
//                .andExpect(jsonPath("$[0].email").value("john@example.com"))
//                .andExpect(jsonPath("$[0].password").value("password_hash"))
//                .andExpect(jsonPath("$[0].user.tariff.name").value("FREE"));
//    }
//
//    @Test
//    void shouldCreateUser() throws Exception {
//        UserFormDto form = new UserFormDto("john_doe", "john@example.com", "password_hash", 1L);
//        UserDto created = new UserDto(1L, "john_doe", "john@example.com", "password_hash", new TariffDto(1L, "FREE"));
//        Mockito.when(userService.insert(any(UserFormDto.class))).thenReturn(created);
//
//        mockMvc.perform(post("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"john_doe\",\"tariffId\":1}"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$[0].username").value("john_doe"))
//                .andExpect(jsonPath("$[0].email").value("john@example.com"))
//                .andExpect(jsonPath("$[0].password").value("password_hash"))
//                .andExpect(jsonPath("$[0].user.tariff.name").value("FREE"));
//    }
//
//    @Test
//    void shouldUpdateUser() throws Exception {
//        UserFormDto form = new UserFormDto("Updated User", "john@example.com", "password_hash", 1L);
//        UserDto updated = new UserDto(1L, "Updated User", "john@example.com", "password_hash", new TariffDto(1L, "FREE"));
//
//        Mockito.when(userService.update(eq(1L), any(UserFormDto.class))).thenReturn(updated);
//
//        mockMvc.perform(put("/api/books/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"Updated User\",\"tariffId\":1}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].username").value("john_doe"))
//                .andExpect(jsonPath("$[0].email").value("john@example.com"))
//                .andExpect(jsonPath("$[0].password").value("password_hash"))
//                .andExpect(jsonPath("$[0].user.tariff.name").value("FREE"));
//    }
//
//    @Test
//    void shouldDeleteUser() throws Exception {
//        mockMvc.perform(delete("/api/users/{id}", 1L))
//                .andExpect(status().isNoContent());
//
//        Mockito.verify(userService).deleteById(1L);
//    }
}