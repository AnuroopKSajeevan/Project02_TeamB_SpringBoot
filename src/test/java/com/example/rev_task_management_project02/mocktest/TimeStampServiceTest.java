package com.example.rev_task_management_project02.mocktest;

import com.example.rev_task_management_project02.exceptions.TimestampNotFoundException;
import com.example.rev_task_management_project02.models.TimeStamp;
import com.example.rev_task_management_project02.dao.TimeStampRepository;
import com.example.rev_task_management_project02.services.TimeStampService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TimeStampServiceTest {

    @InjectMocks
    private TimeStampService timeStampService;

    @Mock
    private TimeStampRepository timeStampRepository;

    @Mock
    private EntityUpdater entityUpdater;

    private TimeStamp timeStamp;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        timeStamp = new TimeStamp(1L, null, null, new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testGetAllTimeStamps() {
        List<TimeStamp> timeStamps = Arrays.asList(timeStamp);
        given(timeStampRepository.findAll()).willReturn(timeStamps);

        List<TimeStamp> result = timeStampService.getAllTimeStamps();
        assertEquals(1, result.size());
        assertEquals(timeStamp.getTimeStampId(), result.get(0).getTimeStampId());
    }

    @Test
    void testGetTimeStampById_Success() throws TimestampNotFoundException {
        given(timeStampRepository.findById(anyLong())).willReturn(Optional.of(timeStamp));

        TimeStamp result = timeStampService.getTimeStampById(1L);
        assertNotNull(result);
        assertEquals(timeStamp.getTimeStampId(), result.getTimeStampId());
    }

    @Test
    void testGetTimeStampById_NotFound() {
        given(timeStampRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(TimestampNotFoundException.class, () -> {
            timeStampService.getTimeStampById(1L);
        });
    }

    @Test
    void testCreateTimeStamp() {
        given(timeStampRepository.save(any(TimeStamp.class))).willReturn(timeStamp);

        TimeStamp result = timeStampService.createTimeStamp(timeStamp);
        assertNotNull(result);
        assertEquals(timeStamp.getTimeStampId(), result.getTimeStampId());
    }

    @Test
    void testUpdateTimeStamp_Success() throws TimestampNotFoundException {
        given(timeStampRepository.findById(anyLong())).willReturn(Optional.of(timeStamp));
        given(entityUpdater.updateFields(any(TimeStamp.class), any(TimeStamp.class))).willReturn(timeStamp);
        given(timeStampRepository.save(any(TimeStamp.class))).willReturn(timeStamp);

        TimeStamp result = timeStampService.updateTimeStamp(1L, timeStamp);
        assertNotNull(result);
        assertEquals(timeStamp.getTimeStampId(), result.getTimeStampId());
    }

    @Test
    void testUpdateTimeStamp_NotFound() {
        given(timeStampRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(TimestampNotFoundException.class, () -> {
            timeStampService.updateTimeStamp(1L, timeStamp);
        });
    }

    @Test
    void testDeleteTimeStamp_Success() throws TimestampNotFoundException {
        given(timeStampRepository.findById(anyLong())).willReturn(Optional.of(timeStamp));
        doNothing().when(timeStampRepository).delete(any(TimeStamp.class));

        timeStampService.deleteTimeStamp(1L);
        verify(timeStampRepository, times(1)).delete(timeStamp);
    }

    @Test
    void testDeleteTimeStamp_NotFound() {
        given(timeStampRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(TimestampNotFoundException.class, () -> {
            timeStampService.deleteTimeStamp(1L);
        });
    }
}