package com.example.rev_task_management_project02.mockmvc;

import com.example.rev_task_management_project02.controllers.UserController;
import com.example.rev_task_management_project02.exceptions.UserNotFoundException;
import com.example.rev_task_management_project02.models.Role;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    public void testLoginSuccess() throws Exception {
        Mockito.when(userService.login(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(get("/api/login")
                        .param("email", "test@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }


    @Test
    public void testRequestPasswordResetSuccess() throws Exception {
        Mockito.doNothing().when(userService).sendPasswordResetToken(anyString());

        mockMvc.perform(post("/api/requestPasswordReset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset token sent to email."));
    }

    @Test
    public void testRequestPasswordResetUserNotFound() throws Exception {
        Mockito.doThrow(new UserNotFoundException("User not found")).when(userService).sendPasswordResetToken(anyString());

        mockMvc.perform(post("/api/requestPasswordReset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));
    }

    @Test
    public void testResetPasswordSuccess() throws Exception {
        Mockito.doNothing().when(userService).resetPasswordWithToken(anyString(), anyString());

        mockMvc.perform(post("/api/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"validtoken\", \"newPassword\":\"newpassword\", \"confirmPassword\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully."));
    }

    @Test
    public void testResetPasswordMismatch() throws Exception {
        mockMvc.perform(post("/api/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"validtoken\", \"newPassword\":\"newpassword\", \"confirmPassword\":\"wrongpassword\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Passwords do not match."));
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/admin/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\", \"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        Mockito.when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/admin/updateUser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"updated@example.com\", \"password\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        Mockito.when(userService.updateUser(anyLong(), any(User.class))).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/api/admin/updateUser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"updated@example.com\", \"password\":\"newpassword\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        Mockito.when(userService.getUserById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/api/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        Mockito.when(userService.getUserById(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/admin/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllUsersSuccess() throws Exception {
        List<User> users = Collections.singletonList(user);
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    public void testDeleteUserByIdSuccess() throws Exception {
        Mockito.when(userService.deleteUserById(anyLong())).thenReturn(user);

        mockMvc.perform(delete("/api/admin/deleteUser/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testDeleteUserByIdNotFound() throws Exception {
        Mockito.when(userService.deleteUserById(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(delete("/api/admin/deleteUser/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeactivateUserSuccess() throws Exception {
        Mockito.when(userService.deactivateUser(anyLong())).thenReturn(user);

        mockMvc.perform(put("/api/admin/deactivateUser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID 1 deactivated successfully."));
    }

    @Test
    public void testDeactivateUserNotFound() throws Exception {
        Mockito.when(userService.deactivateUser(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/api/admin/deactivateUser/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAssignRoleInvalidRole() throws Exception {
        mockMvc.perform(put("/api/admin/assignRole/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"INVALID_ROLE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid role specified."));
    }


}