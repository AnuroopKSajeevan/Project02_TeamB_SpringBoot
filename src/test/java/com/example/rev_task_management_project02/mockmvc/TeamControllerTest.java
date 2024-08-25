package com.example.rev_task_management_project02.mockmvc;


import com.example.rev_task_management_project02.controllers.TeamController;
import com.example.rev_task_management_project02.exceptions.TeamNotFoundException;
import com.example.rev_task_management_project02.models.Team;
import com.example.rev_task_management_project02.services.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    private Team team;

    @BeforeEach
    public void setup() {
        team = new Team();
        team.setTeamId(1L);
        team.setTeamName("Test Team");
    }

    @Test
    public void testCreateTeam() throws Exception {
        when(teamService.createTeam(any(Team.class))).thenReturn(team);

        String teamJson = objectMapper.writeValueAsString(team);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamJson))
                .andExpect(status().isOk())
                .andExpect(content().json(teamJson));
    }

    @Test
    public void testGetTeamById_Success() throws Exception {
        when(teamService.getTeamById(anyLong())).thenReturn(team);

        String teamJson = objectMapper.writeValueAsString(team);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(teamJson));
    }

    @Test
    public void testGetTeamById_NotFound() throws Exception {
        when(teamService.getTeamById(anyLong()))
                .thenThrow(new TeamNotFoundException("Team not found with id " + 1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTeam_Success() throws Exception {
        Team updatedTeam = new Team();
        updatedTeam.setTeamId(1L);
        updatedTeam.setTeamName("Updated Team");

        when(teamService.updateTeam(anyLong(), any(Team.class))).thenReturn(updatedTeam);

        String updatedTeamJson = objectMapper.writeValueAsString(updatedTeam);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/teams/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTeamJson))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedTeamJson));
    }

    @Test
    public void testGetAllTeams() throws Exception {
        List<Team> teams = Arrays.asList(team);
        when(teamService.getAllTeams()).thenReturn(teams);

        String teamsJson = objectMapper.writeValueAsString(teams);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(content().json(teamsJson));
    }
    @Test
    public void testDeleteTeamById_Success() throws Exception {
        Mockito.when(teamService.deleteTeamById(anyLong())).thenReturn(team);

        String teamJson = objectMapper.writeValueAsString(team);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/teams/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(teamJson));
    }


}
