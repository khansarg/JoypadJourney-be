package com.example.joypadjourney.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.joypadjourney.model.entity.Room;

public interface RoomRepository extends JpaRepository<Room, String> {

    // 1. Cari semua room dengan status true (tersedia)
    List<Room> findByStatusTrue();

    // 2. Cari room yang tidak memiliki reservasi pada waktu yang dipilih
    @Query("SELECT r FROM Room r WHERE r.status = true AND r.roomName NOT IN " +
           "(SELECT res.room.roomName FROM Reservation res " +
           " WHERE res.startDateTime < :end AND res.endDateTime > :start)")
    List<Room> findAvailableRooms(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 3. Cari room berdasarkan nama room
    Optional<Room> findByRoomName(String roomName);
}
