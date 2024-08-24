package com.example.rev_task_management_project02.mockmvc;

import com.example.rev_task_management_project02.controllers.ProjectUserController;
import com.example.rev_task_management_project02.models.ProjectUser;
import com.example.rev_task_management_project02.services.ProjectUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectUserController.class)
class ProjectUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectUserService projectUserService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateProjectUser() throws Exception {
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProjectUserId(1L);

        Mockito.when(projectUserService.createProjectUser(any(ProjectUser.class))).thenReturn(projectUser);

        mockMvc.perform(post("/api/project-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectUserId").value(1L));
    }

    @Test
    void testGetProjectUserById() throws Exception {
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProjectUserId(1L);

        Mockito.when(projectUserService.getProjectUserById(1L)).thenReturn(Optional.of(projectUser));

        mockMvc.perform(get("/api/project-users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectUserId").value(1L));
    }

    @Test
    void testGetProjectUserByIdNotFound() throws Exception {
        Mockito.when(projectUserService.getProjectUserById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/project-users/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllProjectUsers() throws Exception {
        ProjectUser projectUser1 = new ProjectUser();
        projectUser1.setProjectUserId(1L);

        ProjectUser projectUser2 = new ProjectUser();
        projectUser2.setProjectUserId(2L);

        Mockito.when(projectUserService.getAllProjectUsers()).thenReturn(Arrays.asList(projectUser1, projectUser2));

        mockMvc.perform(get("/api/project-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].projectUserId").value(1L))
                .andExpect(jsonPath("$[1].projectUserId").value(2L));
    }

    @Test
    void testUpdateProjectUser() throws Exception {
        ProjectUser updatedProjectUser = new ProjectUser();
        updatedProjectUser.setProjectUserId(1L);

        Mockito.when(projectUserService.updateProjectUser(anyLong(), any(ProjectUser.class))).thenReturn(updatedProjectUser);

        mockMvc.perform(put("/api/project-users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectUserId").value(1L));
    }

    @Test
    void testUpdateProjectUserNotFound() throws Exception {
        Mockito.when(projectUserService.updateProjectUser(anyLong(), any(ProjectUser.class)))
                .thenThrow(new RuntimeException("ProjectUser not found"));

        mockMvc.perform(put("/api/project-users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProjectUser())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProjectUser() throws Exception {
        mockMvc.perform(delete("/api/project-users/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(projectUserService, Mockito.times(1)).deleteProjectUser(1L);
    }
}

