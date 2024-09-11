package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.application.dto.ObjetInfoDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetRepository extends JpaRepository<Objet, Long> {

    // 마이룸 - 라운지별 오브제 목록 조회
    List<Objet> findByLoungeIdAndDeletedAtIsNullAndStatusAndObjetSharersUserIdOrderByIdDesc(
            Long loungeId, ObjetStatus status, Long userId
    );

    // 라운지별 오브제 목록 조회
    List<Objet> findByLoungeIdAndDeletedAtIsNullAndStatusOrderByIdDesc(
            Long loungeId, ObjetStatus status
    );

    @Query("""
            SELECT o FROM Objet o 
            JOIN o.objetSharers s 
            WHERE o.lounge.id = :loungeId 
            AND o.deletedAt IS NULL 
            AND o.status = :status 
            AND s.user.id = :userId 
            ORDER BY o.id DESC
            """)
    List<ObjetInfoDto> findActiveObjetsInLoungeOfSharer(@Param("userId") Long userId,
                                                        @Param("loungeId") Long loungeId,
                                                        @Param("status") ObjetStatus status);

    @Query("""
            SELECT o FROM Objet o
            WHERE o.lounge.id = :loungeId
            AND o.deletedAt IS NULL
            AND o.status = :status
            ORDER BY o.id DESC
            """)
    List<ObjetInfoDto> findActiveObjetsInLounge(@Param("loungeId") Long loungeId, @Param("status") ObjetStatus status);


}
