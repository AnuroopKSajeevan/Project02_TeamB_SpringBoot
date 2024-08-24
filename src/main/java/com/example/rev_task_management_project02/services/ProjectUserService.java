package com.example.rev_task_management_project02.services;

import com.example.rev_task_management_project02.dao.ProjectUserRepository;
import com.example.rev_task_management_project02.models.ProjectUser;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;
    private EntityUpdater entityUpdater;

    @Autowired
    public ProjectUserService(ProjectUserRepository projectUserRepository) {
        this.projectUserRepository = projectUserRepository;
    }

    public ProjectUser createProjectUser(ProjectUser projectUser) {
        return projectUserRepository.save(projectUser);
    }

    public Optional<ProjectUser> getProjectUserById(long projectUserId) {
        return projectUserRepository.findById(projectUserId);
    }

    public List<ProjectUser> getAllProjectUsers() {
        return projectUserRepository.findAll();
    }

    public ProjectUser updateProjectUser(long projectUserId, ProjectUser projectUserDetails) {
        ProjectUser projectUser = projectUserRepository.findById(projectUserId)
                .orElseThrow(() -> new RuntimeException("ProjectUser not found with id: " + projectUserId));

        ProjectUser updatedProjectUser = entityUpdater.updateFields(projectUser, projectUserDetails);

        return projectUserRepository.save(updatedProjectUser);
    }


    public void deleteProjectUser(long projectUserId) {
        projectUserRepository.deleteById(projectUserId);
    }
}
