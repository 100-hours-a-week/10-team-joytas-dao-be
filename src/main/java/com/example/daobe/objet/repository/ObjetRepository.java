package com.example.daobe.objet.repository;

import com.example.daobe.objet.entity.Objet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetRepository extends JpaRepository<Objet, Long> {

    // 마이룸 - 라운지별 오브제 목록 조회
    @Query("select o "
            + "from Objet o "
            + "join o.userObjets uo "
            + "join uo.user u "
            + "where o.lounge.loungeId = :loungeId "
            + "and o.deletedAt is null "
            + "and o.status = 'ACTIVE' "
            // TODO : 요청을 보낸 user의 id로 변경 필요
            + "and u.id = 1001"
    )
    List<Objet> findObjetListForOwner(@Param("loungeId") Long loungeId);

    // 라운지별 오브제 목록 조회
    @Query("select o "
            + "from Objet o "
            + "where o.lounge.loungeId=:loungeId "
            + "and o.deletedAt is null "
            + "and o.status = 'ACTIVE'")
    List<Objet> findObjetList(@Param("loungeId") Long loungeId);

}
