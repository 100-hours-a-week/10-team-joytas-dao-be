package com.example.daobe.user.domain.repository;

import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.UserStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(String kakao);

    List<User> findByNicknameContainingAndStatus(String nickname, UserStatus status);
}
