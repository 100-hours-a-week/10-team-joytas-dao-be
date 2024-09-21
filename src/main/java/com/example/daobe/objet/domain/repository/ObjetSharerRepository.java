package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.ObjetStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ObjetSharerRepository extends JpaRepository<ObjetSharer, Long> {

    @Query("""
            SELECT o FROM ObjetSharer os
            JOIN os.objet o
            WHERE os.user.id = :userId
            AND o.status = :status
            ORDER BY o.createdAt DESC
            LIMIT 4
            """)
    List<Objet> findMyRecentObjetByUserId(@Param("userId") Long userId,
                                          @Param("status") ObjetStatus status);

    List<ObjetSharer> findTop4ByUserIdOrderByIdDesc(Long userId);

    List<ObjetSharer> findAllByObjetId(Long objetId);

    @Query("SELECT os.user.id FROM ObjetSharer os WHERE os.objet = :objet")
    List<Long> findSharerIdsByObjet(@Param("objet") Objet objet);

    void deleteAllByObjetAndUserIdIn(Objet objet, List<Long> userIds);

}
