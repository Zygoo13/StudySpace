package com.zygoo132.studyspace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "combo")
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comboId;

    private String comboName;

    @Column(nullable = false)
    private Integer hours;

    private Integer price;
}
