package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.Objet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetRepository extends JpaRepository<Objet, Long> {

    @Query("""
            SELECT o FROM Objet o
            JOIN ObjetSharer s ON o = s.objet
            WHERE o.lounge.id = :loungeId
            AND o.status = 'ACTIVE'
            AND s.user.id = :userId
            ORDER BY o.id DESC
            """)
    List<Objet> findActiveObjetListInLoungeOfSharer(
            @Param("userId") Long userId, @Param("loungeId") Long loungeId
    );

    @Query("""
            SELECT o FROM Objet o
            WHERE o.lounge.id = :loungeId
            AND o.status = 'ACTIVE'
            ORDER BY o.id DESC
            """)
    List<Objet> findActiveObjetListInLounge(
            @Param("loungeId") Long loungeId
    );

    @Query("""
            SELECT o FROM Objet o
            WHERE o.id = :objetId
            AND o.status = 'ACTIVE'
            """)
    Optional<Objet> findByIdAndActiveStatus(@Param("objetId") Long objetId);
}
