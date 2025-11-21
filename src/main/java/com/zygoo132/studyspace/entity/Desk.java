package com.zygoo132.studyspace.entity;

import com.zygoo132.studyspace.enums.LoaiBan;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "desk")
public class Desk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deskId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private String deskName;
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private LoaiBan deskType;

    private String description;
}
