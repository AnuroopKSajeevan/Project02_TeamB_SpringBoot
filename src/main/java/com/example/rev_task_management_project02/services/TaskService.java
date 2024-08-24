package com.example.rev_task_management_project02.services;

import com.example.rev_task_management_project02.dao.TaskRepository;
import com.example.rev_task_management_project02.dao.TimeStampRepository;
import com.example.rev_task_management_project02.exceptions.TaskNotFoundException;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.models.TimeStamp;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EntityUpdater entityUpdater;
    private final TimeStampService timeStampService;
    private final TimeStampRepository timeStampRepository;


    @Autowired
    public TaskService(TaskRepository taskRepository, EntityUpdater entityUpdater, TimeStampService timeStampService, TimeStampRepository timeStampRepository) {
        this.taskRepository = taskRepository;
        this.entityUpdater = entityUpdater;
        this.timeStampService = timeStampService;
        this.timeStampRepository = timeStampRepository;
    }

    public Task getTaskById(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with ID " + id + " not found"));
    }

    public Task createTask(Task newTask) {
        newTask.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newTask.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        Task savedTask = taskRepository.save(newTask);

        if (savedTask.getMilestone() != null) {
            TimeStamp timeStamp = new TimeStamp();
            timeStamp.setTask(savedTask);
            timeStamp.setMilestone(savedTask.getMilestone());
            timeStamp.setTimeStamp(new Timestamp(System.currentTimeMillis()));

            timeStampService.createTimeStamp(timeStamp);
        }

        return savedTask;
    }

    public Task updateTask(Long id, Task updatedTask) throws TaskNotFoundException {
        Task existingTask = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with ID " + id + " not found"));

        existingTask.setMilestone(updatedTask.getMilestone());

        existingTask.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        Task savedTask = taskRepository.save(existingTask);

        if (savedTask.getMilestone() != null) {
            TimeStamp timeStamp = new TimeStamp();
            timeStamp.setTask(savedTask);
            timeStamp.setMilestone(savedTask.getMilestone());
            timeStamp.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            timeStampService.createTimeStamp(timeStamp);
        }

        return savedTask;
    }

    public Task deleteTaskById(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task not found with ID " + id));

        List<TimeStamp> timeStamps = timeStampRepository.findByTaskTaskId(id);
        for (TimeStamp timeStamp : timeStamps) {
            timeStampRepository.delete(timeStamp);
        }

        taskRepository.deleteById(id);

        return task;
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectProjectId(projectId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<TimeStamp> getTimeStampsForTask(long taskId) {
        return timeStampRepository.findByTaskTaskId(taskId);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserUserId(userId);
    }
}
