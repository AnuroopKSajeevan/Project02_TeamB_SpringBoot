package com.example.rev_task_management_project02.controllers;


import com.example.rev_task_management_project02.exceptions.TeamNotFoundException;
import com.example.rev_task_management_project02.models.Task;
import com.example.rev_task_management_project02.models.Team;
import com.example.rev_task_management_project02.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team newTeam) {
        Team createdTeam = teamService.createTeam(newTeam);
        return ResponseEntity.ok(createdTeam);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        try {
            Team team = teamService.getTeamById(id);
            return ResponseEntity.ok(team);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team updatedTeam) {
        try {
            Team team = teamService.updateTeam(id, updatedTeam);
            return ResponseEntity.ok(team);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Team> deleteTeamById(@PathVariable Long id) {
        try {
            Team deletedTeam = teamService.deleteTeamById(id);
            return ResponseEntity.ok(deletedTeam);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<Team> getTeamsByProjectId(@PathVariable Long projectId) {
        Team teams = teamService.getTeamBtProjectId(projectId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }
}
