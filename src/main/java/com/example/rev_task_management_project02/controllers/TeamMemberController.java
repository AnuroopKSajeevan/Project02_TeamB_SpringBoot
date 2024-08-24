package com.example.rev_task_management_project02.controllers;

import com.example.rev_task_management_project02.models.TeamMember;
import com.example.rev_task_management_project02.services.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teamMember")
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;

    @PostMapping
    public void addTeamMember(@RequestParam Long userId, @RequestParam Long teamId) {
        teamMemberService.addTeamMember(userId, teamId);
    }

    @DeleteMapping
    public void removeTeamMember(@RequestParam Long userId, @RequestParam Long teamId) {
        teamMemberService.removeTeamMember(userId, teamId);
    }
    @GetMapping
    public List<TeamMember> getTeamMembers(@RequestParam Long teamId) {
        return teamMemberService.getTeamMembersByTeamId(teamId);
    }
}
