package com.example.webhooksolver.repo;

import com.example.webhooksolver.domain.SolutionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRecordRepository extends JpaRepository<SolutionRecord, Long> {}
