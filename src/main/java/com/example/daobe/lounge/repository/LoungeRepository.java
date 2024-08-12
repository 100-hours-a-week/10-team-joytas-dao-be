package com.example.daobe.lounge.repository;

import com.example.daobe.lounge.entity.Lounge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoungeRepository extends JpaRepository<Lounge, Long> {

}
