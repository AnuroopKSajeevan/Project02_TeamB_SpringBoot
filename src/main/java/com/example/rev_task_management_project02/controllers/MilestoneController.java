package com.example.rev_task_management_project02.controllers;

import com.example.rev_task_management_project02.exceptions.MilestoneNotFoundException;
import com.example.rev_task_management_project02.models.Milestone;
import com.example.rev_task_management_project02.services.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;

    @Autowired
    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    @GetMapping
    public List<Milestone> getAllMilestones() {
        return milestoneService.getAllMilestones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Milestone> getMilestoneById(@PathVariable Long id) {
        try {
            Milestone milestone = milestoneService.getMilestoneById(id);
            return ResponseEntity.ok(milestone);
        } catch (MilestoneNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Milestone createMilestone(@RequestBody Milestone milestone) {
        return milestoneService.createMilestone(milestone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Milestone> updateMilestone(@PathVariable Long id, @RequestBody Milestone milestoneDetails) {
        try {
            Milestone updatedMilestone = milestoneService.updateMilestone(id, milestoneDetails);
            return ResponseEntity.ok(updatedMilestone);
        } catch (MilestoneNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Milestone> deleteMilestone(@PathVariable Long id) {
        try {
            Milestone deletedMilestone = milestoneService.deleteMilestone(id);
            return ResponseEntity.ok(deletedMilestone);
        } catch (MilestoneNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}