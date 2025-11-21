package com.zygoo132.studyspace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "reservation_service")
public class ReservationService {

    @EmbeddedId
    private ReservationServiceId id;

    @ManyToOne
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    private Integer quantity;
}
