package com.example.thisaraprinters.repository;

import com.example.thisaraprinters.model.JobMaterialUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobMaterialUsageRepository extends JpaRepository<JobMaterialUsage, Long> {
    List<JobMaterialUsage> findByJobId(Long jobId);
    List<JobMaterialUsage> findByStockLotId(Long stockLotId);
}
