package com.zygoo132.studyspace.entity;

import com.zygoo132.studyspace.enums.LoaiPhong;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "floor_id")
    private Floor floor;

    private String roomName;

    @Enumerated(EnumType.STRING)
    private LoaiPhong roomType;

    private String description;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Desk> desks;
}
