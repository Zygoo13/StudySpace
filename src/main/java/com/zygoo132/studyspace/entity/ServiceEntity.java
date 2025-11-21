package com.zygoo132.studyspace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    private String serviceName;
    private String description;

    @OneToOne(mappedBy = "service")
    private ServiceInventory inventory;
}


