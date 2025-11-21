package com.zygoo132.studyspace.repository;

import com.zygoo132.studyspace.entity.ServiceInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceInventoryRepository extends JpaRepository<ServiceInventory, Long> {

    Optional<ServiceInventory> findByService_ServiceId(Long serviceId);
}
