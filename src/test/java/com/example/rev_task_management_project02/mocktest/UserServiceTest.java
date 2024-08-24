package com.example.rev_task_management_project02.mocktest;



import com.example.rev_task_management_project02.dao.UserRepository;
import com.example.rev_task_management_project02.exceptions.LoginFailedException;
import com.example.rev_task_management_project02.exceptions.UserNotFoundException;
import com.example.rev_task_management_project02.models.Role;
import com.example.rev_task_management_project02.models.Status;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.services.UserService;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityUpdater entityUpdater;

    @InjectMocks
    private UserService userService;

    private User existingUser;
    private User newUser;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setEmail("existing@example.com");
        existingUser.setUserName("Existing User");
        existingUser.setPassword("password");
        existingUser.setStatus(Status.ACTIVE);
        existingUser.setUserRole(Role.ADMIN);

        newUser = new User();
        newUser.setEmail("updated@example.com");
        newUser.setUserName("Updated User");

        userList = Arrays.asList(existingUser);
    }

    @Test
    void testGetUserById_Success() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        User result = userService.getUserById(1L);

        assertEquals(existingUser, result);
        verify(userRepository).findById(1L);
    }

    @Test
    public void testLogin_Success() throws LoginFailedException {
        when(userRepository.findByEmailAndPassword("existing@example.com", "password")).thenReturn(existingUser);

        User result = userService.login("existing@example.com", "password");

        assertEquals(existingUser, result);
        verify(userRepository).findByEmailAndPassword("existing@example.com", "password");
    }

    @Test
    public void testResetPassword_Success() {
        when(userRepository.findByEmail("existing@example.com")).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.resetPassword("existing@example.com", "newPassword");

        assertEquals("newPassword", result.getPassword());
        verify(userRepository).findByEmail("existing@example.com");
        verify(userRepository).save(existingUser);
    }

    @Test
    public void testCreateUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertEquals(newUser, result);
        verify(userRepository).save(newUser);
    }

    @Test
    public void testUpdateUser_Success() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(entityUpdater.updateFields(existingUser, newUser)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.updateUser(1L, newUser);

        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("Updated User", result.getUserName());
        verify(userRepository).findById(1L);
        verify(userRepository).save(newUser);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(existingUser.getEmail(), result.get(0).getEmail());
        verify(userRepository).findAll();
    }

    @Test
    void testDeactivateUser_Success() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.deactivateUser(1L);

        assertEquals(Status.INACTIVE, result.getStatus());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void testAssignRole_Success() throws UserNotFoundException {
        Role newRole = Role.ADMIN;
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.assignRole(1L, newRole);

        assertEquals(newRole, result.getUserRole());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }
    @Test
    void testDeleteUserById_UserExists() throws UserNotFoundException {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        User deletedUser = userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
        assertEquals(existingUser, deletedUser);
    }

}
