package com.example.rev_task_management_project02.controllers;

import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.exceptions.MilestoneNotFoundException;
import com.example.rev_task_management_project02.models.Milestone;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.models.TimeStamp;
import com.example.rev_task_management_project02.services.TaskService;
import com.example.rev_task_management_project02.services.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final MilestoneService milestoneService;

    @Autowired
    public TaskController(TaskService taskService, MilestoneService milestoneService) {
        this.taskService = taskService;
        this.milestoneService = milestoneService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task newTask) {
        Task createdTask = taskService.createTask(newTask);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            Task task = taskService.getTaskById(id);
            if (updates.containsKey("milestoneId")) {
                Long milestoneId = ((Number) updates.get("milestoneId")).longValue();
                Milestone milestone = milestoneService.getMilestoneById(milestoneId);
                task.setMilestone(milestone);
            }
            Task updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException | MilestoneNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTaskById(id);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}/timestamps")
    public ResponseEntity<List<TimeStamp>> getTaskTimestamps(@PathVariable("taskId") long taskId) {
        List<TimeStamp> timeStamps = taskService.getTimeStampsForTask(taskId);
        return ResponseEntity.ok(timeStamps);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable Long userId) {
        List<Task> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }
}