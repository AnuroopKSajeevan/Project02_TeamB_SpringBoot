package com.example.rev_task_management_project02.mocktest;

import com.example.rev_task_management_project02.dao.TaskRepository;
import com.example.rev_task_management_project02.dao.TimeStampRepository;
import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.models.Task;

import com.example.rev_task_management_project02.services.TaskService;
import com.example.rev_task_management_project02.services.TimeStampService;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EntityUpdater entityUpdater;

    @Mock
    private TimeStampService timeStampService;

    @Mock
    private TimeStampRepository timeStampRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Test Task");
        task.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        task.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testGetTaskById_Positive() throws TaskNotFoundException {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals(task.getTaskId(), foundTask.getTaskId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_Negative() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(1L);
        });

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateTask_Positive() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        assertEquals(task.getTaskId(), createdTask.getTaskId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTask_Positive() throws TaskNotFoundException {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTask(1L, task);

        assertNotNull(updatedTask);
        assertEquals(task.getTaskId(), updatedTask.getTaskId());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTask_Negative() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(1L, task);
        });

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testDeleteTaskById_Positive() throws TaskNotFoundException {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(timeStampRepository.findByTaskTaskId(1L)).thenReturn(new ArrayList<>());

        Task deletedTask = taskService.deleteTaskById(1L);

        assertNotNull(deletedTask);
        assertEquals(task.getTaskId(), deletedTask.getTaskId());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTaskById_Negative() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTaskById(1L);
        });

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(0)).deleteById(1L);
    }

    @Test
    void testGetTasksByProjectId_Positive() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        when(taskRepository.findByProjectProjectId(1L)).thenReturn(tasks);

        List<Task> foundTasks = taskService.getTasksByProjectId(1L);

        assertNotNull(foundTasks);
        assertEquals(1, foundTasks.size());
        verify(taskRepository, times(1)).findByProjectProjectId(1L);
    }

    @Test
    void testGetAllTasks_Positive() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> foundTasks = taskService.getAllTasks();

        assertNotNull(foundTasks);
        assertEquals(1, foundTasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTasksByUserId_Positive() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        when(taskRepository.findByUserUserId(1L)).thenReturn(tasks);

        List<Task> foundTasks = taskService.getTasksByUserId(1L);

        assertNotNull(foundTasks);
        assertEquals(1, foundTasks.size());
        verify(taskRepository, times(1)).findByUserUserId(1L);
    }
}