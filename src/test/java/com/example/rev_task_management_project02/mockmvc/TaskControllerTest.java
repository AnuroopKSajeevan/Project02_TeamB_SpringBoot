package com.example.rev_task_management_project02.mockmvc;

import com.example.rev_task_management_project02.controllers.TaskController;
import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.services.MilestoneService;
import com.example.rev_task_management_project02.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private MilestoneService milestoneService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Sample Task");
    }

    @Test
    void getTaskById_Success() throws Exception {
        when(taskService.getTaskById(anyLong())).thenReturn(task);

        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskId").value(task.getTaskId()))
                .andExpect(jsonPath("$.taskName").value(task.getTaskName()));
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        when(taskService.getTaskById(anyLong())).thenThrow(new TaskNotFoundException("Task not found"));

        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTasks_Success() throws Exception {
        List<Task> tasks = Collections.singletonList(task);
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].taskId").value(task.getTaskId()))
                .andExpect(jsonPath("$[0].taskName").value(task.getTaskName()));
    }

    @Test
    void createTask_Success() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskId").value(task.getTaskId()))
                .andExpect(jsonPath("$.taskName").value(task.getTaskName()));
    }

    @Test
    void updateTask_Success() throws Exception {
        when(taskService.getTaskById(anyLong())).thenReturn(task);
        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskId").value(task.getTaskId()))
                .andExpect(jsonPath("$.taskName").value(task.getTaskName()));
    }

    @Test
    void updateTask_NotFound() throws Exception {
        when(taskService.getTaskById(anyLong())).thenThrow(new TaskNotFoundException("Task not found"));

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_Success() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_NotFound() throws Exception {
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTaskById(anyLong());

        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}