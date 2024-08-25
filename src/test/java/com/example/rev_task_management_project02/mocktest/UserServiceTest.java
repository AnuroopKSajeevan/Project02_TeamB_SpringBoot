package com.example.rev_task_management_project02.mocktest;

import com.example.rev_task_management_project02.dao.UserRepository;
import com.example.rev_task_management_project02.exceptions.LoginFailedException;
import com.example.rev_task_management_project02.exceptions.UserNotFoundException;
import com.example.rev_task_management_project02.models.Role;
import com.example.rev_task_management_project02.models.Status;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.services.MailService;
import com.example.rev_task_management_project02.services.UserService;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityUpdater entityUpdater;

    @Mock
    private MailService mailService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "John Doe", Role.ADMIN, "john.doe@example.com", "password123", "1234567890", 0L, Status.ACTIVE, "Engineering", null);
    }

    @Test
    void testLoginSuccess() throws LoginFailedException {
        when(userRepository.findByEmailAndPassword("john.doe@example.com", "password123")).thenReturn(user);

        User loggedInUser = userService.login("john.doe@example.com", "password123");

        assertNotNull(loggedInUser);
        assertEquals("John Doe", loggedInUser.getUserName());
    }

    @Test
    void testLoginFailure() {
        when(userRepository.findByEmailAndPassword("john.doe@example.com", "wrongpassword")).thenReturn(null);

        assertThrows(LoginFailedException.class, () -> userService.login("john.doe@example.com", "wrongpassword"));
    }
    
    @Test
    void testResetPasswordWithTokenFailure() {
        String token = UUID.randomUUID().toString();

        assertThrows(UserNotFoundException.class, () -> userService.resetPasswordWithToken(token, "newpassword123"));
    }



    @Test
    void testUpdateUserSuccess() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(entityUpdater.updateFields(any(User.class), any(User.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, user);

        assertNotNull(updatedUser);
        assertEquals("John Doe", updatedUser.getUserName());
    }

    @Test
    void testUpdateUserFailure() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, user));
    }

    @Test
    void testGetUserByIdSuccess() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getUserName());
    }

    @Test
    void testGetUserByIdFailure() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testDeleteUserByIdSuccess() throws UserNotFoundException {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User deletedUser = userService.deleteUserById(1L);

        assertNotNull(deletedUser);
        assertEquals("John Doe", deletedUser.getUserName());
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserByIdFailure() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));
    }

    @Test
    void testDeactivateUserSuccess() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User deactivatedUser = userService.deactivateUser(1L);

        assertNotNull(deactivatedUser);
        assertEquals(Status.INACTIVE, deactivatedUser.getStatus());
    }

    @Test
    void testDeactivateUserFailure() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deactivateUser(1L));
    }

    @Test
    void testAssignRoleSuccess() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.assignRole(1L, Role.ADMIN);

        assertNotNull(updatedUser);
        assertEquals(Role.ADMIN, updatedUser.getUserRole());
    }

    @Test
    void testAssignRoleFailure() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.assignRole(1L, Role.ADMIN));
    }
}