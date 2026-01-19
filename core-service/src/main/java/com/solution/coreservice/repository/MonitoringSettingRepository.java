package com.solution.coreservice.repository;

import com.solution.coreservice.entity.MonitoringSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringSettingRepository extends JpaRepository<MonitoringSetting, Long> {
}
