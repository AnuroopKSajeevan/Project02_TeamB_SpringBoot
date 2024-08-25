package com.example.rev_task_management_project02.services;

import com.example.rev_task_management_project02.dao.UserRepository;
import com.example.rev_task_management_project02.exceptions.LoginFailedException;
import com.example.rev_task_management_project02.exceptions.UserNotFoundException;
import com.example.rev_task_management_project02.models.Role;
import com.example.rev_task_management_project02.models.Status;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private MailService mailService;

    private final UserRepository userRepository;
    private final EntityUpdater entityUpdater;

    private Map<String, String> tokenStore = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository, EntityUpdater entityUpdater) {
        this.userRepository = userRepository;
        this.entityUpdater = entityUpdater;
    }


    public User login(String email, String password) throws LoginFailedException {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new LoginFailedException("Invalid email or password. Login failed.");
        }
        return user;
    }

    public void sendPasswordResetToken(String email) throws UserNotFoundException, MessagingException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }

        String token = UUID.randomUUID().toString();
        tokenStore.put(token, email);

        String subject = "Password Reset Request";
        String body = String.format("Dear %s,\n\nTo reset your password, please use the following token: %s\n\nBest regards,\nTeam Synergize",
                user.getUserName(), token);
        mailService.sendEmail(user.getEmail(), subject, body);
    }

    public void resetPasswordWithToken(String token, String newPassword) throws UserNotFoundException {
        String email = tokenStore.get(token);
        if (email == null) {
            throw new UserNotFoundException("Invalid token.");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        tokenStore.remove(token);
    }


public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkPassword(User user, String oldPassword) {
        return oldPassword.equals(user.getPassword());
    }

public User createUser(User user) {
    User createdUser = userRepository.save(user);

    try {
        String subject = "Your New Account Password";
        String body = String.format("Dear %s,\n\nYour account has been created successfully. Your password is: %s\n\nBest regards,\nTeam Synergize",
                createdUser.getUserName(), user.getPassword());
        mailService.sendEmail(user.getEmail(), subject, body);
    } catch (MessagingException e) {
        e.printStackTrace();  // Log or handle the exception as needed
    }

    return createdUser;
}
    public User updateUser(Long userId, User newDetails) throws UserNotFoundException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        User updatedUser = entityUpdater.updateFields(existingUser, newDetails);
        return userRepository.save(updatedUser);
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User not found with id " + id)
        );
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User deleteUserById(Long id) throws UserNotFoundException {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).get();
            userRepository.deleteById(id);
            return user;
        } else {
            throw new UserNotFoundException("User not found with id " + id);
        }
    }
    public User deactivateUser(Long id) throws UserNotFoundException {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setStatus(Status.INACTIVE);
            return userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("User not found with id " + id);
        }
    }
    public User assignRole(Long id, Role role) throws UserNotFoundException {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setUserRole(role);
            return userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("User not found with id " + id);
        }
    }
}
