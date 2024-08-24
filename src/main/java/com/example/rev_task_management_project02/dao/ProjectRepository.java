package com.example.rev_task_management_project02.dao;

import com.example.rev_task_management_project02.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findByManager_UserId(Long managerId);

}
