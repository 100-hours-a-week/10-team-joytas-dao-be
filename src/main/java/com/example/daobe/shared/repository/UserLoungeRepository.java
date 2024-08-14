package com.example.daobe.shared.repository;

import com.example.daobe.shared.entity.UserLounge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLoungeRepository extends JpaRepository<UserLounge, Long> {

    @Query("SELECT CASE WHEN COUNT(ul) > 0 THEN true ELSE false END "
            + "FROM UserLounge ul WHERE ul.user.id = :userId "
            + "AND ul.lounge.id = :loungeId")
    boolean existsByUserIdAndLoungeId(@Param("userId") Long userId, @Param("loungeId") Long loungeId);
}
