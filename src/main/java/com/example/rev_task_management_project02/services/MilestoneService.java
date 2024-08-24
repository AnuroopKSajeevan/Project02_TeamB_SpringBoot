package com.example.rev_task_management_project02.services;

import com.example.rev_task_management_project02.dao.MilestoneRepository;
import com.example.rev_task_management_project02.exceptions.MilestoneNotFoundException;
import com.example.rev_task_management_project02.models.Milestone;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilestoneService {

    private final EntityUpdater entityUpdater;
    private final MilestoneRepository milestoneRepository;

    @Autowired
    public MilestoneService(EntityUpdater entityUpdater, MilestoneRepository milestoneRepository) {
        this.entityUpdater = entityUpdater;
        this.milestoneRepository = milestoneRepository;
    }

    public List<Milestone> getAllMilestones() {
        return milestoneRepository.findAll();
    }

    public Milestone getMilestoneById(Long id) throws MilestoneNotFoundException {
        return milestoneRepository.findById(id).orElseThrow(() ->
                new MilestoneNotFoundException("Milestone not found with id " + id));
    }

    public Milestone createMilestone(Milestone milestone) {
        return milestoneRepository.save(milestone);
    }

    public Milestone updateMilestone(Long id, Milestone milestoneDetails) throws MilestoneNotFoundException {
        Milestone existingMilestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new MilestoneNotFoundException("Milestone with Id " + id + " not found"));
        Milestone updatedMilestone = entityUpdater.updateFields(existingMilestone, milestoneDetails);
        return milestoneRepository.save(updatedMilestone);
    }

    public Milestone deleteMilestone(Long id) throws MilestoneNotFoundException {
        if (milestoneRepository.existsById(id)) {
            Milestone milestone = milestoneRepository.findById(id).get();
            milestoneRepository.deleteById(id);
            return milestone;
        } else {
            throw new MilestoneNotFoundException("Milestone not found with id " + id);
        }
    }
}