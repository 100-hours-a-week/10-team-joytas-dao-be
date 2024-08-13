package com.example.daobe.user.repository;

import com.example.daobe.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(String kakao);
}
