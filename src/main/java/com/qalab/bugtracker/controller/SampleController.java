package com.qalab.bugtracker.controller;

import com.qalab.bugtracker.model.SampleEntity;
import com.qalab.bugtracker.repository.SampleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/samples")
public class SampleController {

    private final SampleRepository sampleRepository;

    public SampleController(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @GetMapping
    public List<SampleEntity> getAllSamples() {
        return sampleRepository.findAll();
    }

    @PostMapping
    public SampleEntity createSample(@RequestBody SampleEntity sample) {
        return sampleRepository.save(sample);
    }
}
