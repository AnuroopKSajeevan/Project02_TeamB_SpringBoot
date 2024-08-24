package com.example.rev_task_management_project02.services;


import com.example.rev_task_management_project02.dao.ProjectRepository;
import com.example.rev_task_management_project02.dao.TaskRepository;
import com.example.rev_task_management_project02.exceptions.ProjectNotFoundException;
import com.example.rev_task_management_project02.models.Project;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.models.Team;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EntityUpdater entityUpdater;
    private final TeamService teamService;
    private TaskRepository taskRepository;


    @Autowired
    public ProjectService(ProjectRepository projectRepository, EntityUpdater entityUpdater, TeamService teamService,TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.entityUpdater = entityUpdater;
        this.teamService=teamService;
        this.taskRepository =  taskRepository;
    }

    public Project getProjectById(Long id) throws ProjectNotFoundException {
        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project with ID " + id + " not found"));
    }

    public Project createProject(Project project, String teamName) {
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
