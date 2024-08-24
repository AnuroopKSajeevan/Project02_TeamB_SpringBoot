package com.example.rev_task_management_project02.services;


import com.example.rev_task_management_project02.dao.ProjectRepository;
import com.example.rev_task_management_project02.dao.TaskRepository;
import com.example.rev_task_management_project02.exceptions.ProjectNotFoundException;
import com.example.rev_task_management_project02.exceptions.UserNotFoundException;
import com.example.rev_task_management_project02.models.*;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EntityUpdater entityUpdater;
    private final TeamService teamService;
    private TaskRepository taskRepository;
    private final ClientService clientService;
    private final UserService userService;




    @Autowired
    public ProjectService(ProjectRepository projectRepository, EntityUpdater entityUpdater, TeamService teamService,TaskRepository taskRepository,ClientService clientService,UserService userService) {
        this.projectRepository = projectRepository;
        this.entityUpdater = entityUpdater;
        this.teamService=teamService;
        this.taskRepository =  taskRepository;
        this.clientService = clientService;
        this.userService = userService;
    }

    public Project getProjectById(Long id) throws ProjectNotFoundException {
        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project with ID " + id + " not found"));
    }

    public Project createProject(Project project, String teamName) throws UserNotFoundException {
        if (project.getClient() == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }

        Client client = clientService.getClientById(project.getClient().getClientId())
                .orElseThrow(() -> new ResourceAccessException("Client not found"));
        project.setClient(client);

        User manager = userService.getUserById(project.getManager().getUserId());
        if (manager == null) {
            throw new UserNotFoundException("User not found with id: " + project.getManager().getUserId());
        }
        project.setManager(manager);

        Project savedProject = projectRepository.save(project);
        Team team = new Team();
        team.setTeamName(teamName);
        team.setManager(savedProject.getManager());
        team.setProject(savedProject);

        teamService.createTeam(team);

        return savedProject;
    }


    public Project updateProject(Long id, Project updatedProject) throws ProjectNotFoundException {
        Project existingProject = projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project with ID " + id + " not found"));

        Project updateProject = entityUpdater.updateFields(existingProject, updatedProject);

        return projectRepository.save(updateProject);
    }

    public Project deleteProjectById(Long id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project not found with ID " + id));

        List<Task> tasks = taskRepository.findByProjectProjectId(id);
        for (Task task : tasks) {
            taskRepository.delete(task);
        }

        projectRepository.deleteById(id);

        return project;
    }



    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    public List<Project> getProjectsByManagerId(Long managerId) {
        return projectRepository.findByManager_UserId(managerId);
    }
}
