package com.example.daobe.objet.repository;

import com.example.daobe.objet.entity.Objet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetRepository extends JpaRepository<Objet, Long>, ObjetCustomRepository {
}
