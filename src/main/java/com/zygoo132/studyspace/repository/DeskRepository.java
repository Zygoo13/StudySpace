package com.zygoo132.studyspace.repository;

import com.zygoo132.studyspace.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DeskRepository extends JpaRepository<Desk, Long> {

    @Query("""
        SELECT d FROM Desk d
        WHERE d.room.roomId = :roomId
          AND d.deskId NOT IN (
               SELECT d2.deskId FROM Reservation r
               JOIN r.desks d2
               WHERE r.startTime < :end
                 AND r.endTime > :start
          )
    """)
    List<Desk> findAvailableDesksInRoom(
            @Param("roomId") Long roomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // findByRoom_RoomId
    List<Desk> findByRoom_RoomId(Long roomId);

}
