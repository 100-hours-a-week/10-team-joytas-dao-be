package com.example.daobe.myroom.domain.repository;

import com.example.daobe.myroom.domain.MyRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyRoomRepository extends JpaRepository<MyRoom, Long> {

    Optional<MyRoom> findByUserId(Long userId);
}
