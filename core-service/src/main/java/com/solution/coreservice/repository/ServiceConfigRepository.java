package com.solution.coreservice.repository;

import com.solution.coreservice.entity.ServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceConfigRepository extends JpaRepository<ServiceConfig, Long> {

}
