package com.example.rev_task_management_project02.mocktest;


import com.example.rev_task_management_project02.dao.TeamRepository;
import com.example.rev_task_management_project02.exceptions.TeamNotFoundException;
import com.example.rev_task_management_project02.models.Team;
import com.example.rev_task_management_project02.services.TeamService;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private EntityUpdater entityUpdater;

    @InjectMocks
    private TeamService teamService;

    private Team existingTeam;
    private Team newTeam;
    private List<Team> teamList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingTeam = new Team();
        existingTeam.setTeamId(1L);
        existingTeam.setTeamName("Existing Team");

        newTeam = new Team();
        newTeam.setTeamName("New Team");

        teamList = Arrays.asList(existingTeam);
    }

    @Test
    void testGetTeamById_Success() throws TeamNotFoundException {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(existingTeam));

        Team result = teamService.getTeamById(1L);

        assertEquals(existingTeam, result);
        verify(teamRepository).findById(1L);
    }

    @Test
    void testCreateTeam_Success() {
        when(teamRepository.save(any(Team.class))).thenReturn(newTeam);

        Team result = teamService.createTeam(newTeam);

        assertEquals(newTeam, result);
        verify(teamRepository).save(newTeam);
    }

    @Test
    void testGetAllTeams() {
        when(teamRepository.findAll()).thenReturn(teamList);

        List<Team> result = teamService.getAllTeams();

        assertEquals(1, result.size());
        assertEquals(existingTeam.getTeamName(), result.get(0).getTeamName());
        verify(teamRepository).findAll();
    }

    @Test
    void testUpdateTeam_Success() throws TeamNotFoundException {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(entityUpdater.updateFields(existingTeam, newTeam)).thenReturn(newTeam);
        when(teamRepository.save(newTeam)).thenReturn(newTeam);

        Team result = teamService.updateTeam(1L, newTeam);

        assertNotNull(result);
        assertEquals("New Team", result.getTeamName());
        verify(teamRepository).findById(1L);
        verify(teamRepository).save(newTeam);
    }

    @Test
    void testDeleteTeamById_Success() throws TeamNotFoundException {
        when(teamRepository.existsById(1L)).thenReturn(true);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(existingTeam));

        Team deletedTeam = teamService.deleteTeamById(1L);

        verify(teamRepository).deleteById(1L);
        assertEquals(existingTeam, deletedTeam);
    }

    @Test
    void testGetTeamById_NotFound() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamById(1L));
        verify(teamRepository).findById(1L);
    }

    @Test
    void testDeleteTeamById_NotFound() {
        when(teamRepository.existsById(1L)).thenReturn(false);

        assertThrows(TeamNotFoundException.class, () -> teamService.deleteTeamById(1L));
        verify(teamRepository, never()).deleteById(anyLong());
    }
}
