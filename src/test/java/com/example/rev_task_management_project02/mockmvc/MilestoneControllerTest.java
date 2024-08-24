package com.example.rev_task_management_project02.mockmvc;

import com.example.rev_task_management_project02.controllers.MilestoneController;
import com.example.rev_task_management_project02.exceptions.MilestoneNotFoundException;
import com.example.rev_task_management_project02.models.Milestone;
import com.example.rev_task_management_project02.services.MilestoneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MilestoneController.class)
class MilestoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MilestoneService milestoneService;

    @Autowired
    private ObjectMapper objectMapper;

    private Milestone milestone;

    @BeforeEach
    public void setUp() {
        milestone = new Milestone();
        milestone.setMilestoneId(1L);
        milestone.setMilestoneName("Test Milestone");
        milestone.setMilestoneDescription("This is a test milestone.");
        milestone.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        milestone.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testGetAllMilestones() throws Exception {
        List<Milestone> milestones = Arrays.asList(milestone);
        when(milestoneService.getAllMilestones()).thenReturn(milestones);

        mockMvc.perform(get("/api/milestones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].milestoneName").value(milestone.getMilestoneName()));
    }

    @Test
    void testGetMilestoneById_Success() throws Exception {
        when(milestoneService.getMilestoneById(anyLong())).thenReturn(milestone);

        mockMvc.perform(get("/api/milestones/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneName").value(milestone.getMilestoneName()));
    }

    @Test
    void testGetMilestoneById_NotFound() throws Exception {
        when(milestoneService.getMilestoneById(anyLong())).thenThrow(new MilestoneNotFoundException("Milestone not found with id 1"));

        mockMvc.perform(get("/api/milestones/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMilestone() throws Exception {
        when(milestoneService.createMilestone(any(Milestone.class))).thenReturn(milestone);

        mockMvc.perform(post("/api/milestones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(milestone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneName").value(milestone.getMilestoneName()));
    }

    @Test
    void testUpdateMilestone_Success() throws Exception {
        when(milestoneService.updateMilestone(anyLong(), any(Milestone.class))).thenReturn(milestone);

        mockMvc.perform(put("/api/milestones/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(milestone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneName").value(milestone.getMilestoneName()));
    }

    @Test
    void testUpdateMilestone_NotFound() throws Exception {
        when(milestoneService.updateMilestone(anyLong(), any(Milestone.class))).thenThrow(new MilestoneNotFoundException("Milestone not found with id 1"));

        mockMvc.perform(put("/api/milestones/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(milestone)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMilestone_Success() throws Exception {
        when(milestoneService.deleteMilestone(anyLong())).thenReturn(milestone);

        mockMvc.perform(delete("/api/milestones/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneName").value(milestone.getMilestoneName()));
    }

    @Test
    void testDeleteMilestone_NotFound() throws Exception {
        when(milestoneService.deleteMilestone(anyLong())).thenThrow(new MilestoneNotFoundException("Milestone not found with id 1"));

        mockMvc.perform(delete("/api/milestones/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}