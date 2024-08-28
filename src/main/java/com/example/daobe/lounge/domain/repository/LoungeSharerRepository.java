package com.example.daobe.lounge.domain.repository;

import com.example.daobe.lounge.domain.LoungeSharer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoungeSharerRepository extends JpaRepository<LoungeSharer, Long> {

    @Query("SELECT CASE WHEN COUNT(ls) > 0 THEN true ELSE false END "
            + "FROM LoungeSharer ls WHERE ls.user.id = :userId "
            + "AND ls.lounge.id = :loungeId")
    boolean existsByUserIdAndLoungeId(@Param("userId") Long userId, @Param("loungeId") Long loungeId);

    List<LoungeSharer> findByLounge_IdAndUser_NicknameContaining(Long loungeId, String nickname);
}
