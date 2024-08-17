package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.ObjetSharer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjetSharerRepository extends JpaRepository<ObjetSharer, Long> {

    List<ObjetSharer> findByUserId(Long userId);
}
