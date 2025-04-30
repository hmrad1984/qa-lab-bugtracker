package com.qalab.bugtracker.repository;

import com.qalab.bugtracker.model.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
}
