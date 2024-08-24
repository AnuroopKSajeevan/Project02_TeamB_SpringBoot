package com.example.rev_task_management_project02.mockmvc;


import com.example.rev_task_management_project02.controllers.ProjectController;
import com.example.rev_task_management_project02.exceptions.ProjectNotFoundException;
import com.example.rev_task_management_project02.models.Project;
import com.example.rev_task_management_project02.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project project;

    @BeforeEach
    public void setup() {
        project = new Project();
        project.setProjectId(1L);
        project.setProjectName("Test Project");
    }

    @Test
    public void testCreateProject() throws Exception {
        String teamName = "Development Team";

        Project project = new Project();
        project.setProjectId(1L);
        project.setProjectName("New Project");
        project.setDescription("This is a new project.");

        String projectJson = objectMapper.writeValueAsString(project);

        Mockito.when(projectService.createProject(any(Project.class), any(String.class)))
                .thenReturn(project);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects")
                        .param("teamName", teamName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(projectJson));
    }

    @Test
    public void testGetProjectById_Success() throws Exception {
        Mockito.when(projectService.getProjectById(anyLong())).thenReturn(project);

        String projectJson = objectMapper.writeValueAsString(project);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(projectJson));
    }

    @Test
    public void testGetProjectById_NotFound() throws Exception {
        Mockito.when(projectService.getProjectById(anyLong()))
                .thenThrow(new ProjectNotFoundException("Project not found with id " + 1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProjectById_Success() throws Exception {
        Mockito.when(projectService.deleteProjectById(anyLong())).thenReturn(project);

        String projectJson = objectMapper.writeValueAsString(project);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(projectJson));
    }

    @Test
    public void testGetAllProjects() throws Exception {
        List<Project> projects = Arrays.asList(project);
        Mockito.when(projectService.getAllProjects()).thenReturn(projects);

        String projectsJson = objectMapper.writeValueAsString(projects);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(content().json(projectsJson));
    }
    @Test
    public void testUpdateProject_Success() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setProjectId(1L);
        updatedProject.setProjectName("Updated Project");

        Mockito.when(projectService.updateProject(anyLong(), any(Project.class))).thenReturn(updatedProject);

        String updatedProjectJson = objectMapper.writeValueAsString(updatedProject);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProjectJson))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedProjectJson));
    }
}
