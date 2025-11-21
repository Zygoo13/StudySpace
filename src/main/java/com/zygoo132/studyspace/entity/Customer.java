package com.zygoo132.studyspace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "customer",
        uniqueConstraints = @UniqueConstraint(columnNames = "phone"))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    private String note;
}
