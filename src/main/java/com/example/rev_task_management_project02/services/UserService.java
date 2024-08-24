package com.example.rev_task_management_project02.services;



import com.example.rev_task_management_project02.dao.UserRepository;
import com.example.rev_task_management_project02.exceptions.LoginFailedException;
import com.example.rev_task_management_project02.exceptions.UserNotFoundException;
import com.example.rev_task_management_project02.models.Role;
import com.example.rev_task_management_project02.models.Status;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityUpdater entityUpdater;

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

    public User resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            return userRepository.save(user);
        }
        return null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkPassword(User user, String oldPassword) {
        return oldPassword.equals(user.getPassword());
    }
    public User createUser(User user){
        return userRepository.save(user);
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
