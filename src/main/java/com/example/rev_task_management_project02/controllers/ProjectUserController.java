package com.example.rev_task_management_project02.controllers;

import com.example.rev_task_management_project02.models.ProjectUser;
import com.example.rev_task_management_project02.services.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project-users")
public class ProjectUserController {

    private final ProjectUserService projectUserService;

    @Autowired
    public ProjectUserController(ProjectUserService projectUserService) {
        this.projectUserService = projectUserService;
    }

    @PostMapping
    public ResponseEntity<ProjectUser> createProjectUser(@RequestBody ProjectUser projectUser) {
        ProjectUser createdProjectUser = projectUserService.createProjectUser(projectUser);
        return ResponseEntity.ok(createdProjectUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectUser> getProjectUserById(@PathVariable long id) {
        Optional<ProjectUser> projectUser = projectUserService.getProjectUserById(id);
        return projectUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProjectUser>> getAllProjectUsers() {
        List<ProjectUser> projectUsers = projectUserService.getAllProjectUsers();
        return ResponseEntity.ok(projectUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectUser> updateProjectUser(@PathVariable long id, @RequestBody ProjectUser projectUserDetails) {
        try {
            ProjectUser updatedProjectUser = projectUserService.updateProjectUser(id, projectUserDetails);
            return ResponseEntity.ok(updatedProjectUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectUser(@PathVariable long id) {
        projectUserService.deleteProjectUser(id);
        return ResponseEntity.noContent().build();
    }
}
