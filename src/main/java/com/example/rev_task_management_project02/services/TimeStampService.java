package com.example.rev_task_management_project02.services;

import com.example.rev_task_management_project02.exceptions.TimestampNotFoundException;
import com.example.rev_task_management_project02.models.TimeStamp;
import com.example.rev_task_management_project02.dao.TimeStampRepository;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeStampService {

    @Autowired
    private TimeStampRepository timeStampRepository;

    @Autowired
    private EntityUpdater entityUpdater;

    public List<TimeStamp> getAllTimeStamps() {
        return timeStampRepository.findAll();
    }

    public TimeStamp getTimeStampById(long id) throws TimestampNotFoundException {
        Optional<TimeStamp> timeStamp = timeStampRepository.findById(id);
        if (timeStamp.isPresent()) {
            return timeStamp.get();
        } else {
            throw new TimestampNotFoundException("Timestamp not found with id: " + id);
        }
    }

    public TimeStamp createTimeStamp(TimeStamp timeStamp) {
        return timeStampRepository.save(timeStamp);
    }

    public TimeStamp updateTimeStamp(long id, TimeStamp timeStampDetails) throws TimestampNotFoundException {
        TimeStamp existingTimeStamp = getTimeStampById(id);
        TimeStamp updatedTimeStamp = entityUpdater.updateFields(existingTimeStamp, timeStampDetails);
        return timeStampRepository.save(updatedTimeStamp);
    }

    public void deleteTimeStamp(long id) throws TimestampNotFoundException {
        TimeStamp timeStamp = getTimeStampById(id);
        timeStampRepository.delete(timeStamp);
    }
}