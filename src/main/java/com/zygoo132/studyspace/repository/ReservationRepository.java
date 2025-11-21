package com.zygoo132.studyspace.repository;

import com.zygoo132.studyspace.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Lấy reservation đang ACTIVE tại thời điểm hiện tại cho 1 bàn
     * (dùng trong trang /desks/status)
     */
    @Query("""
        SELECT r FROM Reservation r
        JOIN r.desks d
        WHERE d.deskId = :deskId
          AND r.startTime <= :now
          AND r.endTime >= :now
        ORDER BY r.startTime
    """)
    List<Reservation> findActiveReservation(
            @Param("deskId") Long deskId,
            @Param("now") LocalDateTime now
    );

    /**
     * Lấy reservation theo ngày (dùng cho lịch đặt theo ngày)
     */
    @Query("""
        SELECT r FROM Reservation r
        WHERE r.startTime >= :startOfDay
          AND r.startTime <= :endOfDay
        ORDER BY r.startTime
    """)
    List<Reservation> findByDay(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    /**
     * Kiểm tra trùng giờ của 1 bàn (dùng nếu bạn muốn validate nâng cao)
     * Kiểu: (start < end_new) AND (end > start_new)
     */
        @Query("""
            SELECT r FROM Reservation r
            JOIN r.desks d
            WHERE d.deskId = :deskId
              AND r.startTime < :endNew
              AND r.endTime > :startNew
        """)
        List<Reservation> findOverlappingReservation(
                @Param("deskId") Long deskId,
                @Param("startNew") LocalDateTime startNew,
                @Param("endNew") LocalDateTime endNew
        );
}
