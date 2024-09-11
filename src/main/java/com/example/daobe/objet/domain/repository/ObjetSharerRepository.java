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
            AND o.deletedAt IS NULL
            ORDER BY o.createdAt DESC
            LIMIT 4
            """)
    List<Objet> findMyRecentObjetByUserId(@Param("userId") Long userId,
                                          @Param("status") ObjetStatus status);


}
