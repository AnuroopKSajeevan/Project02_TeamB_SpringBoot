package com.example.rev_task_management_project02.mocktest;

import com.example.rev_task_management_project02.dao.TaskRepository;
import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.services.TaskService;
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

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EntityUpdater entityUpdater;

    @InjectMocks
    private TaskService taskService;

    private Task existingTask;
    private Task newTask;
    private List<Task> taskList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingTask = new Task();
        existingTask.setTaskId(1L);
        existingTask.setTaskName("Existing Task");

        newTask = new Task();
        newTask.setTaskName("New Task");

        taskList = Arrays.asList(existingTask);
    }

    @Test
    void testGetTaskById_Success() throws TaskNotFoundException {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        Task result = taskService.getTaskById(1L);

        assertEquals(existingTask, result);
        verify(taskRepository).findById(1L);
    }

    @Test
    void testCreateTask_Success() {
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        Task result = taskService.createTask(newTask);

        assertEquals(newTask, result);
        verify(taskRepository).save(newTask);
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(taskList);

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals(existingTask.getTaskName(), result.get(0).getTaskName());
        verify(taskRepository).findAll();
    }

    @Test
    public void testUpdateTask_Success() throws TaskNotFoundException {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(entityUpdater.updateFields(existingTask, newTask)).thenReturn(newTask);
        when(taskRepository.save(newTask)).thenReturn(newTask);

        Task result = taskService.updateTask(1L, newTask);

        assertNotNull(result);
        assertEquals("New Task", result.getTaskName());
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(newTask);
    }

    @Test
    void testDeleteTaskById_Success() throws TaskNotFoundException {
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        Task deletedTask = taskService.deleteTaskById(1L);

        verify(taskRepository, times(1)).deleteById(1L);
        assertEquals(existingTask, deletedTask);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository).findById(1L);
    }

    @Test
    void testDeleteTaskById_NotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(1L));
        verify(taskRepository, never()).deleteById(anyLong());
    }

}
