package com.example.rev_task_management_project02.mocktest;


import com.example.rev_task_management_project02.dao.ProjectRepository;
import com.example.rev_task_management_project02.dao.TeamRepository;
import com.example.rev_task_management_project02.exceptions.ProjectNotFoundException;
import com.example.rev_task_management_project02.models.Project;
import com.example.rev_task_management_project02.models.Team;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.services.ProjectService;
import com.example.rev_task_management_project02.services.TeamService;
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

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamService teamService;


    @Mock
    private EntityUpdater entityUpdater;


    @InjectMocks
    private ProjectService projectService;

    private Project existingProject;
    private Project newProject;
    private List<Project> projectList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingProject = new Project();
        existingProject.setProjectId(1L);
        existingProject.setProjectName("Existing Project");

        newProject = new Project();
        newProject.setProjectName("New Project");

        projectList = Arrays.asList(existingProject);
    }

    @Test
    void testGetProjectById_Success() throws ProjectNotFoundException {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));

        Project result = projectService.getProjectById(1L);

        assertEquals(existingProject, result);
        verify(projectRepository).findById(1L);
    }

    @Test
    void testCreateProject_Success() {
        String teamName = "Development Team";
        Project savedProject = new Project();
        savedProject.setProjectId(1L);
        savedProject.setProjectName("New Project");

        User mockManager = mock(User.class);
        savedProject.setManager(mockManager);

        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        Team newTeam = new Team();
        when(teamService.createTeam(any(Team.class))).thenReturn(newTeam);

        Project result = projectService.createProject(savedProject, teamName);

        assertEquals(savedProject, result);
        verify(projectRepository).save(savedProject);
        verify(teamService).createTeam(argThat(team ->
                team.getTeamName().equals(teamName) &&
                        team.getProject().equals(savedProject) &&
                        team.getManager().equals(savedProject.getManager())
        ));
    }



    @Test
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(projectList);

        List<Project> result = projectService.getAllProjects();

        assertEquals(1, result.size());
        assertEquals(existingProject.getProjectName(), result.get(0).getProjectName());
        verify(projectRepository).findAll();
    }

    @Test
    public void testUpdateProject_Success() throws ProjectNotFoundException {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(entityUpdater.updateFields(existingProject, newProject)).thenReturn(newProject);
        when(projectRepository.save(newProject)).thenReturn(newProject);

        Project result = projectService.updateProject(1L, newProject);

        assertNotNull(result);
        assertEquals("New Project", result.getProjectName());
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(newProject);
    }


    @Test
    void testDeleteProjectById_Success() throws ProjectNotFoundException {
        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));

        Project deletedProject = projectService.deleteProjectById(1L);

        verify(projectRepository, times(1)).deleteById(1L);
        assertEquals(existingProject, deletedProject);
    }
    @Test
    void testGetProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(1L));
        verify(projectRepository).findById(1L);
    }

    @Test
    void testDeleteProjectById_NotFound() {
        when(projectRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProjectById(1L));
        verify(projectRepository, never()).deleteById(anyLong());
    }

}
