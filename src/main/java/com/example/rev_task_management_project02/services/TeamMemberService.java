package com.example.rev_task_management_project02.services;


import com.example.rev_task_management_project02.dao.TeamMemberRepository;
import com.example.rev_task_management_project02.dao.TeamRepository;
import com.example.rev_task_management_project02.dao.UserRepository;
import com.example.rev_task_management_project02.models.Team;
import com.example.rev_task_management_project02.models.TeamMember;
import com.example.rev_task_management_project02.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamMemberService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    public void addTeamMember(Long userId, Long teamId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamMember teamMember = new TeamMember();
        teamMember.setUser(user);
        teamMember.setTeam(team);

        teamMemberRepository.save(teamMember);

        User manager = team.getManager();
        if (manager != null) {
            user.setManagerId(manager.getUserId());
        }

        userRepository.save(user);
    }

    public void removeTeamMember(Long userId, Long teamId) {
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndUserId(teamId, userId);
        if (teamMember != null) {
            teamMemberRepository.delete(teamMember);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setManagerId(0);
            userRepository.save(user);
        }
    }
    public List<TeamMember> getTeamMembersByTeamId(Long teamId) {
        return teamMemberRepository.findByTeamId(teamId);
    }
}
