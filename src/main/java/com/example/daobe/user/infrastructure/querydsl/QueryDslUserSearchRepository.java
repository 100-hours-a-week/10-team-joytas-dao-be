package com.example.daobe.user.infrastructure.querydsl;

import static com.example.daobe.user.domain.QUser.user;

import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.UserStatus;
import com.example.daobe.user.domain.repository.UserSearchRepository;
import com.example.daobe.user.domain.repository.dto.UserSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslUserSearchRepository implements UserSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<User> searchByCondition(UserSearchCondition condition) {
        List<User> result = queryFactory.selectFrom(user)
                .where(
                        userNicknameContains(condition.nickname()),
                        userIdLt(condition.cursor()),
                        user.status.eq(UserStatus.ACTIVE)
                )
                .orderBy(user.id.desc())
                .limit(condition.executeLimitSize())
                .fetch();

        boolean hasNext = false;
        if (result.size() == condition.executeLimitSize()) {
            result.remove(result.size() - 1);
            hasNext = true;
        }

        return new SliceImpl<>(result, PageRequest.ofSize(condition.executeLimitSize()), hasNext);
    }

    private BooleanExpression userNicknameContains(String nickname) {
        return nickname == null ? null : user.nickname.contains(nickname);
    }

    private BooleanExpression userIdLt(Long cursor) {
        return cursor == null ? null : user.id.lt(cursor);
    }
}
