package com.example.rev_task_management_project02.controllers;


import com.example.rev_task_management_project02.exceptions.ProjectNotFoundException;
import com.example.rev_task_management_project02.exceptions.UserNotFoundException;
import com.example.rev_task_management_project02.models.Project;
import com.example.rev_task_management_project02.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project, @RequestParam String teamName) throws UserNotFoundException {
        Project createdProject = projectService.createProject(project, teamName);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        try {
            Project updatedProject = projectService.updateProject(id, projectDetails);
            return ResponseEntity.ok(updatedProject);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProjectById(@PathVariable Long id) {
        try {
            Project deletedProject = projectService.deleteProjectById(id);
            return ResponseEntity.ok(deletedProject);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        try {
            Project project = projectService.getProjectById(id);
            return ResponseEntity.ok(project);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/by-manager/{managerId}")
    public List<Project> getProjectsByManagerId(@PathVariable Long managerId) {
        return projectService.getProjectsByManagerId(managerId);
    }
}
