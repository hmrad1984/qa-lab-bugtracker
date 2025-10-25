package com.qalab.bugtracker.unit.controller;

import com.qalab.bugtracker.controller.SampleController;
import com.qalab.bugtracker.model.SampleEntity;
import com.qalab.bugtracker.repository.SampleRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Unit tests for SampleController endpoints.
 */
@WebMvcTest(SampleController.class)
public class SampleControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SampleRepository sampleRepository;

    @Test
    void shouldReturnListOfSamples() throws Exception {
        // Given
        SampleEntity sample = new SampleEntity();
        sample.setName("Mock Sample");

        Mockito.when(sampleRepository.findAll()).thenReturn(List.of(sample));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/samples"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Mock Sample")));
    }

    @Test
    void shouldCreateNewSample() throws Exception {
        // Given
        SampleEntity sample = new SampleEntity();
        sample.setName("New Sample");

        Mockito.when(sampleRepository.save(any(SampleEntity.class))).thenReturn(sample);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Sample\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Sample")));
    }
}
