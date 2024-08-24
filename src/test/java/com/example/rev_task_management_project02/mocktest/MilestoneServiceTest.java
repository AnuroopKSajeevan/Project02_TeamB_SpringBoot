package com.example.rev_task_management_project02.mocktest;

import com.example.rev_task_management_project02.dao.MilestoneRepository;
import com.example.rev_task_management_project02.exceptions.MilestoneNotFoundException;
import com.example.rev_task_management_project02.models.Milestone;
import com.example.rev_task_management_project02.services.MilestoneService;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MilestoneServiceTest {

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private EntityUpdater entityUpdater;

    @InjectMocks
    private MilestoneService milestoneService;

    private Milestone milestone;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        milestone = new Milestone();
        milestone.setMilestoneId(1L);
        milestone.setMilestoneName("Test Milestone");
        milestone.setMilestoneDescription("This is a test milestone.");
        milestone.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        milestone.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testGetAllMilestones() {
        List<Milestone> milestones = Arrays.asList(milestone);
        when(milestoneRepository.findAll()).thenReturn(milestones);

        List<Milestone> result = milestoneService.getAllMilestones();
        assertEquals(1, result.size());
        assertEquals(milestone.getMilestoneName(), result.get(0).getMilestoneName());
    }

    @Test
    void testGetMilestoneById_Success() throws MilestoneNotFoundException {
        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.of(milestone));

        Milestone result = milestoneService.getMilestoneById(1L);
        assertEquals(milestone.getMilestoneName(), result.getMilestoneName());
    }

    @Test
    void testGetMilestoneById_NotFound() {
        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MilestoneNotFoundException.class, () -> {
            milestoneService.getMilestoneById(1L);
        });
    }

    @Test
    void testCreateMilestone() {
        when(milestoneRepository.save(any(Milestone.class))).thenReturn(milestone);

        Milestone result = milestoneService.createMilestone(milestone);
        assertEquals(milestone.getMilestoneName(), result.getMilestoneName());
    }

    @Test
    void testUpdateMilestone_Success() throws MilestoneNotFoundException {
        Milestone updatedMilestone = new Milestone();
        updatedMilestone.setMilestoneId(1L);
        updatedMilestone.setMilestoneName("Updated Milestone");
        updatedMilestone.setMilestoneDescription("This is an updated milestone.");
        updatedMilestone.setCreatedAt(milestone.getCreatedAt());
        updatedMilestone.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.of(milestone));
        when(entityUpdater.updateFields(any(Milestone.class), any(Milestone.class))).thenReturn(updatedMilestone);
        when(milestoneRepository.save(any(Milestone.class))).thenReturn(updatedMilestone);

        Milestone result = milestoneService.updateMilestone(1L, updatedMilestone);
        assertEquals(updatedMilestone.getMilestoneName(), result.getMilestoneName());
    }

    @Test
    void testUpdateMilestone_NotFound() {
        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.empty());

        Milestone updatedMilestone = new Milestone();
        updatedMilestone.setMilestoneId(1L);
        updatedMilestone.setMilestoneName("Updated Milestone");
        updatedMilestone.setMilestoneDescription("This is an updated milestone.");
        updatedMilestone.setCreatedAt(milestone.getCreatedAt());
        updatedMilestone.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        assertThrows(MilestoneNotFoundException.class, () -> {
            milestoneService.updateMilestone(1L, updatedMilestone);
        });
    }

    @Test
    void testDeleteMilestone_Success() throws MilestoneNotFoundException {
        when(milestoneRepository.existsById(anyLong())).thenReturn(true);
        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.of(milestone));

        Milestone result = milestoneService.deleteMilestone(1L);
        assertEquals(milestone.getMilestoneName(), result.getMilestoneName());
        verify(milestoneRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMilestone_NotFound() {
        when(milestoneRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(MilestoneNotFoundException.class, () -> {
            milestoneService.deleteMilestone(1L);
        });
    }
}