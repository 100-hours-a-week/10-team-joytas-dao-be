package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetRepository extends JpaRepository<Objet, Long> {

    // 마이룸 - 라운지별 오브제 목록 조회
    List<Objet> findByLoungeIdAndDeletedAtIsNullAndStatusAndUserObjetsUserId(
            Long loungeId, ObjetStatus status, Long userId
    );

    // 라운지별 오브제 목록 조회
    List<Objet> findByLoungeIdAndDeletedAtIsNullAndStatus(
            Long loungeId, ObjetStatus status
    );

}
