package com.zygoo132.studyspace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "service_inventory")
public class ServiceInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @OneToOne
    @JoinColumn(name = "service_id", nullable = false, unique = true)
    private ServiceEntity service;

    private Integer totalQuantity;
}
