package com.example.rev_task_management_project02.controllers;

import com.example.rev_task_management_project02.exceptions.TimestampNotFoundException;
import com.example.rev_task_management_project02.models.TimeStamp;
import com.example.rev_task_management_project02.services.TimeStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timestamps")
public class TimeStampController {

    @Autowired
    private TimeStampService timeStampService;

    @GetMapping
    public List<TimeStamp> getAllTimeStamps() {
        return timeStampService.getAllTimeStamps();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeStamp> getTimeStampById(@PathVariable long id) {
        try {
            TimeStamp timeStamp = timeStampService.getTimeStampById(id);
            return ResponseEntity.ok(timeStamp);
        } catch (TimestampNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public TimeStamp createTimeStamp(@RequestBody TimeStamp timeStamp) {
        return timeStampService.createTimeStamp(timeStamp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeStamp> updateTimeStamp(@PathVariable long id, @RequestBody TimeStamp timeStampDetails) {
        try {
            TimeStamp updatedTimeStamp = timeStampService.updateTimeStamp(id, timeStampDetails);
            return ResponseEntity.ok(updatedTimeStamp);
        } catch (TimestampNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeStamp(@PathVariable long id) {
        try {
            timeStampService.deleteTimeStamp(id);
            return ResponseEntity.noContent().build();
        } catch (TimestampNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}