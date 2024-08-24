package com.example.rev_task_management_project02.mockmvc;

import com.example.rev_task_management_project02.controllers.TeamMemberController;
import com.example.rev_task_management_project02.services.TeamMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamMemberController.class)
public class TeamMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamMemberService teamMemberService;

    private Long userId;
    private Long teamId;

    @BeforeEach
    public void setUp() {
        userId = 1L;
        teamId = 1L;
    }

    @Test
    public void testAddTeamMember() throws Exception {
        Mockito.doNothing().when(teamMemberService).addTeamMember(userId, teamId);

        mockMvc.perform(post("/api/teamMember")
                        .param("userId", String.valueOf(userId))
                        .param("teamId", String.valueOf(teamId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(teamMemberService, Mockito.times(1)).addTeamMember(userId, teamId);
    }

    @Test
    public void testRemoveTeamMember() throws Exception {
        Mockito.doNothing().when(teamMemberService).removeTeamMember(userId, teamId);

        mockMvc.perform(delete("/api/teamMember")
                        .param("userId", String.valueOf(userId))
                        .param("teamId", String.valueOf(teamId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(teamMemberService, Mockito.times(1)).removeTeamMember(userId, teamId);
    }
}
