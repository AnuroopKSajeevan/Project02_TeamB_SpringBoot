package com.example.rev_task_management_project02.dao;

import com.example.rev_task_management_project02.models.Project;
import com.example.rev_task_management_project02.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findTeamByProject_ProjectId(Long projectId);

}
