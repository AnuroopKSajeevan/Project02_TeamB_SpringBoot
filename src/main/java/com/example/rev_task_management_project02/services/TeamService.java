package com.example.rev_task_management_project02.services;


import com.example.rev_task_management_project02.dao.TeamRepository;
import com.example.rev_task_management_project02.exceptions.TeamNotFoundException;
import com.example.rev_task_management_project02.models.Team;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final EntityUpdater entityUpdater;

    @Autowired
    public TeamService(TeamRepository teamRepository, EntityUpdater entityUpdater) {
        this.teamRepository = teamRepository;
        this.entityUpdater = entityUpdater;
    }

    public Team getTeamById(Long id) throws TeamNotFoundException {
        return teamRepository.findById(id).orElseThrow(() ->
                new TeamNotFoundException("Team with ID " + id + " not found"));
    }


    public Team createTeam(Team newTeam) {
        return teamRepository.save(newTeam);
    }

    public Team updateTeam(Long id, Team updatedTeam) throws TeamNotFoundException {
        Team existingTeam = teamRepository.findById(id).orElseThrow(() ->
                new TeamNotFoundException("Team with ID " + id + " not found"));

        Team updatedTeamEntity = entityUpdater.updateFields(existingTeam, updatedTeam);

        return teamRepository.save(updatedTeamEntity);
    }

    public Team deleteTeamById(Long id) throws TeamNotFoundException {
        if (teamRepository.existsById(id)) {
            Team team = teamRepository.findById(id).get();
            teamRepository.deleteById(id);
            return team;
        } else {
            throw new TeamNotFoundException("Team not found with ID " + id);
        }
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
