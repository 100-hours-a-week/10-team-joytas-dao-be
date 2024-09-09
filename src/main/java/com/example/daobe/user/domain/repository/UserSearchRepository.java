package com.example.daobe.user.domain.repository;

import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.repository.dto.UserSearchCondition;
import org.springframework.data.domain.Slice;

public interface UserSearchRepository {

    Slice<User> searchByCondition(UserSearchCondition condition);
}
