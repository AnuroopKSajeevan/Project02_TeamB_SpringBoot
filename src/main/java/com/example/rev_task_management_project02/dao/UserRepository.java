package com.example.rev_task_management_project02.dao;

import com.example.rev_task_management_project02.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Add custom query methods if needed
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
}
