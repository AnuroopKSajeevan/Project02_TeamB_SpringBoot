package com.example.rev_task_management_project02.mockmvc;


import com.example.rev_task_management_project02.controllers.TaskController;
import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.services.TaskService;
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

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    @BeforeEach
    public void setup() {
        task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Test Task");
    }

    @Test
    public void testCreateTask() throws Exception {
        Mockito.when(taskService.createTask(any(Task.class))).thenReturn(task);

        String taskJson = objectMapper.writeValueAsString(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(content().json(taskJson));
    }

    @Test
    public void testGetTaskById_Success() throws Exception {
        Mockito.when(taskService.getTaskById(anyLong())).thenReturn(task);

        String taskJson = objectMapper.writeValueAsString(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(taskJson));
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        Mockito.when(taskService.getTaskById(anyLong()))
                .thenThrow(new TaskNotFoundException("Task not found with id " + 1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTaskById_Success() throws Exception {
        Mockito.when(taskService.deleteTaskById(anyLong())).thenReturn(task);

        String taskJson = objectMapper.writeValueAsString(task);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<Task> tasks = Arrays.asList(task);
        Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

        String tasksJson = objectMapper.writeValueAsString(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json(tasksJson));
    }

    @Test
    public void testUpdateTask_Success() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTaskId(1L);
        updatedTask.setTaskName("Updated Task");

        Mockito.when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(updatedTask);

        String updatedTaskJson = objectMapper.writeValueAsString(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedTaskJson));
    }
}
