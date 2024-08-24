package com.example.rev_task_management_project02.dao;

import com.example.rev_task_management_project02.models.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    @Query("SELECT tm FROM TeamMember tm WHERE tm.team.teamId = :teamId")
    List<TeamMember> findByTeamId(@Param("teamId") Long teamId);


    @Query("SELECT tm FROM TeamMember tm WHERE tm.team.teamId = :teamId AND tm.user.userId = :userId")
    TeamMember findByTeamIdAndUserId(@Param("teamId") Long teamId, @Param("userId") Long userId);
}
