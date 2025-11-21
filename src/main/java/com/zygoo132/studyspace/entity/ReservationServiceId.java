package com.zygoo132.studyspace.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class ReservationServiceId implements Serializable {

    private Long reservationId;
    private Long serviceId;

    public ReservationServiceId() {}

    public ReservationServiceId(Long reservationId, Long serviceId) {
        this.reservationId = reservationId;
        this.serviceId = serviceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, serviceId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReservationServiceId)) return false;
        ReservationServiceId other = (ReservationServiceId) obj;
        return Objects.equals(reservationId, other.reservationId) &&
                Objects.equals(serviceId, other.serviceId);
    }
}
