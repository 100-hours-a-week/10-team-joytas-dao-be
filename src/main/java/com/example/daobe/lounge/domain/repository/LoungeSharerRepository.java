package com.example.daobe.lounge.domain.repository;

import com.example.daobe.lounge.domain.LoungeSharer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoungeSharerRepository extends JpaRepository<LoungeSharer, Long> {

    @Query("""
            SELECT CASE WHEN EXISTS (
                SELECT 1
                FROM LoungeSharer ls
                WHERE ls.user.id = :userId
                AND ls.lounge.id = :loungeId
                AND ls.status = 'ACTIVE'
            ) THEN true ELSE false END
            """)
    boolean existsActiveLoungeSharerByUserIdAndLoungeId(@Param("userId") Long userId, @Param("loungeId") Long loungeId);

    @Query("""
            SELECT CASE WHEN EXISTS (
                SELECT 1
                FROM LoungeSharer ls
                WHERE ls.user.id = :userId
                AND ls.lounge.id = :loungeId
            ) THEN true ELSE false END
            """)
    boolean existsLoungeSharerByUserIdAndLoungeId(@Param("userId") Long userId, @Param("loungeId") Long loungeId);

    List<LoungeSharer> findByLounge_IdAndUser_NicknameContaining(Long loungeId, String nickname);

    @Query("""
                SELECT COUNT(ls) FROM LoungeSharer ls
                JOIN ls.lounge l
                WHERE l.status = 'ACTIVE'
                AND ls.status = 'ACTIVE'
                AND ls.user.id = :userId
            """)
    long countActiveLoungeSharerByUserId(@Param("userId") Long userId);

    void deleteByUserIdAndLoungeId(Long userId, Long loungeId);

    LoungeSharer findByUserIdAndLoungeId(Long userId, Long loungeId);
}
