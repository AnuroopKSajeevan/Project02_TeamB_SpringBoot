package com.example.rev_task_management_project02.mockmvc;

import com.example.rev_task_management_project02.controllers.UserController;
import com.example.rev_task_management_project02.models.Role;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testLogin_Success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");

        when(userService.login(anyString(), anyString())).thenReturn(mockUser);

        mockMvc.perform(get("/api/login")
                        .param("email", "test@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"test@example.com\",\"password\":\"password\"}"));
    }

    @Test
    void testLogin_Failure() throws Exception {
        when(userService.login(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(get("/api/login")
                        .param("email", "wrong@example.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testResetPassword_Success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("newpassword");

        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);
        when(userService.checkPassword(mockUser, "oldpassword")).thenReturn(true);
        when(userService.resetPassword("test@example.com", "newpassword")).thenReturn(mockUser);

        mockMvc.perform(get("/api/resetPassword")
                        .param("email", "test@example.com")
                        .param("oldPassword", "oldpassword")
                        .param("newPassword", "newpassword")
                        .param("confirmPassword", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }

    @Test
    void testResetPassword_PasswordsDoNotMatch() throws Exception {
        mockMvc.perform(get("/api/resetPassword")
                        .param("email", "test@example.com")
                        .param("oldPassword", "oldpassword")
                        .param("newPassword", "newpassword")
                        .param("confirmPassword", "differentpassword"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Passwords do not match"));
    }

    @Test
    void testResetPassword_OldPasswordIncorrect() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("oldpassword");

        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);
        when(userService.checkPassword(mockUser, "wrongoldpassword")).thenReturn(false);

        mockMvc.perform(get("/api/resetPassword")
                        .param("email", "test@example.com")
                        .param("oldPassword", "wrongoldpassword")
                        .param("newPassword", "newpassword")
                        .param("confirmPassword", "newpassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Old password is incorrect"));
    }

    @Test
    void testResetPassword_UserNotFound() throws Exception {
        when(userService.findByEmail("nonexistent@example.com")).thenReturn(null);

        mockMvc.perform(get("/api/resetPassword")
                        .param("email", "nonexistent@example.com")
                        .param("oldPassword", "oldpassword")
                        .param("newPassword", "newpassword")
                        .param("confirmPassword", "newpassword"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testResetPassword_Failure() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("oldpassword");

        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);
        when(userService.checkPassword(mockUser, "oldpassword")).thenReturn(true);
        when(userService.resetPassword("test@example.com", "newpassword")).thenReturn(null);

        mockMvc.perform(get("/api/resetPassword")
                        .param("email", "test@example.com")
                        .param("oldPassword", "oldpassword")
                        .param("newPassword", "newpassword")
                        .param("confirmPassword", "newpassword"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to reset password"));
    }

    @Test
    void testCreateUser() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("newuser@example.com");
        mockUser.setPassword("password");

        when(userService.createUser(Mockito.any(User.class))).thenReturn(mockUser);

        String userJson = "{\"email\":\"newuser@example.com\",\"password\":\"password\"}";

        mockMvc.perform(post("/api/admin/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(userJson));
    }

    @Test
    void testDeactivateUser_Success() throws Exception {
        when(userService.deactivateUser(anyLong())).thenReturn(new User());

        mockMvc.perform(put("/api/admin/deactivateUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID 1 deactivated successfully."));
    }

    @Test
    void testAssignRole_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserRole(Role.ADMIN);

        when(userService.assignRole(anyLong(), any(Role.class))).thenReturn(mockUser);

        String roleJson = "{\"role\":\"ADMIN\"}";

        mockMvc.perform(put("/api/admin/assignRole/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Role ADMIN assigned to user with ID 1 successfully."));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setUserId(1L);

        when(userService.getUserById(anyLong())).thenReturn(mockUser);

        mockMvc.perform(get("/api/admin/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"test@example.com\",\"userId\":1}"));
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        List<User> userList = Arrays.asList(mockUser);

        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"email\":\"test@example.com\"}]"));
    }

    @Test
    void testDeleteUserById_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setEmail("deleted@example.com");

        when(userService.deleteUserById(anyLong())).thenReturn(mockUser);

        mockMvc.perform(delete("/api/admin/deleteUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"userId\":1,\"email\":\"deleted@example.com\"}"));
    }
}