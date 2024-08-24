package com.example.rev_task_management_project02.mockmvc;

import com.example.rev_task_management_project02.controllers.TimeStampController;
import com.example.rev_task_management_project02.exceptions.TimestampNotFoundException;
import com.example.rev_task_management_project02.models.TimeStamp;
import com.example.rev_task_management_project02.services.TimeStampService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimeStampController.class)
class TimeStampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeStampService timeStampService;

    @Autowired
    private ObjectMapper objectMapper;

    private TimeStamp timeStamp;

    @BeforeEach
    public void setUp() {
        timeStamp = new TimeStamp(1L, null, null, new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testGetAllTimeStamps() throws Exception {
        List<TimeStamp> timeStamps = Arrays.asList(timeStamp);
        given(timeStampService.getAllTimeStamps()).willReturn(timeStamps);

        mockMvc.perform(get("/api/timestamps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timeStampId").value(timeStamp.getTimeStampId()));
    }

    @Test
    void testGetTimeStampById_Success() throws Exception {
        given(timeStampService.getTimeStampById(anyLong())).willReturn(timeStamp);

        mockMvc.perform(get("/api/timestamps/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStampId").value(timeStamp.getTimeStampId()));
    }

    @Test
    void testGetTimeStampById_NotFound() throws Exception {
        given(timeStampService.getTimeStampById(anyLong())).willThrow(new TimestampNotFoundException("Timestamp not found"));

        mockMvc.perform(get("/api/timestamps/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTimeStamp() throws Exception {
        given(timeStampService.createTimeStamp(any(TimeStamp.class))).willReturn(timeStamp);

        mockMvc.perform(post("/api/timestamps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timeStamp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStampId").value(timeStamp.getTimeStampId()));
    }

    @Test
    void testUpdateTimeStamp_Success() throws Exception {
        given(timeStampService.updateTimeStamp(anyLong(), any(TimeStamp.class))).willReturn(timeStamp);

        mockMvc.perform(put("/api/timestamps/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timeStamp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStampId").value(timeStamp.getTimeStampId()));
    }

    @Test
    void testUpdateTimeStamp_NotFound() throws Exception {
        given(timeStampService.updateTimeStamp(anyLong(), any(TimeStamp.class))).willThrow(new TimestampNotFoundException("Timestamp not found"));

        mockMvc.perform(put("/api/timestamps/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timeStamp)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTimeStamp_Success() throws Exception {
        mockMvc.perform(delete("/api/timestamps/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteTimeStamp_NotFound() throws Exception {
        willThrow(new TimestampNotFoundException("Timestamp not found")).given(timeStampService).deleteTimeStamp(anyLong());

        mockMvc.perform(delete("/api/timestamps/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}