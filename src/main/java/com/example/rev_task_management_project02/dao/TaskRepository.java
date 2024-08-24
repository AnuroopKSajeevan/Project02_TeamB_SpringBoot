package com.example.rev_task_management_project02.dao;

import com.example.rev_task_management_project02.models.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByProjectProjectId(Long projectId);
    List<Task> findByUserUserId(Long userId);
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.user.userId = :userId WHERE t.taskId = :taskId AND t.project.projectId = :projectId")
    void updateUserIdByTaskIdAndProjectId(Long taskId, String userId, Long projectId);

}
