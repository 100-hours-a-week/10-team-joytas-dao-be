package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetStatus;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetRepository extends JpaRepository<Objet, Long> {

    // 마이룸 - 라운지별 오브제 목록 조회
    List<Objet> findByLoungeIdAndDeletedAtIsNullAndStatusAndObjetSharersUserId(
            Long loungeId, ObjetStatus status, Long userId
    );

    // 라운지별 오브제 목록 조회
    List<Objet> findByLoungeIdAndDeletedAtIsNullAndStatus(
            Long loungeId, ObjetStatus status
    );

    // 특정 라운지 내에서 사용자가 생성했거나, 공유자로 포함된 오브제 목록 조회
    @Query("SELECT DISTINCT o FROM Objet o "
            + "LEFT JOIN FETCH o.objetSharers os "
            + "WHERE o.lounge.id= :loungeId "
            + "AND (o.user.id = :userId OR os.user.id = :userId)")
    List<Objet> findLoungeObjetByUser(@Param("loungeId") Long loungeId, @Param("userId") Long userId);

}
