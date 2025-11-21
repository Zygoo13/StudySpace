package com.zygoo132.studyspace.repository;

import com.zygoo132.studyspace.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByFloor_FloorId(Long floorId);
}
