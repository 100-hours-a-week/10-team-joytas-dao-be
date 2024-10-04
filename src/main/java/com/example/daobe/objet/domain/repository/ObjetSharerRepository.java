package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.ObjetSharerStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ObjetSharerRepository extends JpaRepository<ObjetSharer, Long> {

    @Query("""
            SELECT os FROM ObjetSharer os JOIN FETCH Objet o
            ON os.objet.id = o.id
            WHERE o.status = 'ACTIVE' AND os.status='ACTIVE' AND os.user.id = :userId
            ORDER BY o.createdAt DESC
            LIMIT 4
            """)
    List<ObjetSharer> findTop4ByUserIdAndActiveStatus(Long userId);

    @Query("SELECT os FROM ObjetSharer os WHERE os.id=:objetId AND os.status = 'ACTIVE'")
    List<ObjetSharer> findAllByObjetIdAndActiveStatus(Long objetId);

    @Query("SELECT os.user.id FROM ObjetSharer os WHERE os.objet = :objet AND os.status = 'ACTIVE'")
    List<Long> findSharerIdListByObjet(@Param("objet") Objet objet);

    void deleteAllByObjetAndUserIdIn(Objet objet, List<Long> userIds);

    @Query("""
            SELECT os FROM ObjetSharer os JOIN FETCH Objet o
            ON os.objet.id = o.id
            WHERE os.user.id = :userId AND o.lounge.id = :loungeId AND os.status = :objetSharerStatus
            """)
    List<ObjetSharer> findByUserIdAndLoungeId(Long userId, Long loungeId, ObjetSharerStatus objetSharerStatus);

}
