package com.example.daobe.user.domain.repository;

import com.example.daobe.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u FROM User u
             WHERE u.id = :userId
             AND u.status != 'DELETED'
            """)
    Optional<User> findByIdAndStatusIsNotDeleted(Long userId);

    Optional<User> findByKakaoId(String kakao);

    boolean existsByNickname(String nickname);
}
