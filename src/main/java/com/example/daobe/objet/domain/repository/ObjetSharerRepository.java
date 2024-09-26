package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ObjetSharerRepository extends JpaRepository<ObjetSharer, Long> {

    List<ObjetSharer> findTop4ByUserIdOrderByIdDesc(Long userId);

    List<ObjetSharer> findAllByObjetId(Long objetId);

    @Query("SELECT os.user.id FROM ObjetSharer os WHERE os.objet = :objet")
    List<Long> findSharerIdListByObjet(@Param("objet") Objet objet);

    void deleteAllByObjetAndUserIdIn(Objet objet, List<Long> userIds);

}
