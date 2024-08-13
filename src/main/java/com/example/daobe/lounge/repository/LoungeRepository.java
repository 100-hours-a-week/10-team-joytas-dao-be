package com.example.daobe.lounge.repository;

import com.example.daobe.lounge.entity.Lounge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoungeRepository extends JpaRepository<Lounge, Long> {

    @Query("SELECT l FROM Lounge l WHERE l.user.id = :userId")
    List<Lounge> findLoungeByUserId(@Param("userId") Long userId);
}
