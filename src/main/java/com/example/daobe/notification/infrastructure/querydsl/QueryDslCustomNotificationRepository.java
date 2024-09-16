package com.example.daobe.notification.infrastructure.querydsl;

import static com.example.daobe.notification.domain.QNotification.notification;

import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.repository.CustomNotificationRepository;
import com.example.daobe.notification.domain.repository.dto.NotificationFindCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryDslCustomNotificationRepository implements CustomNotificationRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Notification> findNotificationByCondition(NotificationFindCondition condition) {
        List<Notification> result = queryFactory.selectFrom(notification)
                .where(
                        notificationIdLt(condition.cursor()),
                        notification.receiveUser.id.eq(condition.userId())
                )
                .limit(condition.executeLimitSize())
                .orderBy(notification.id.desc())
                .fetch();

        boolean hasNext = false;
        if (result.size() == condition.executeLimitSize()) {
            result.remove(result.size() - 1);
            hasNext = true;
        }
        return new SliceImpl<>(result, PageRequest.ofSize(condition.executeLimitSize()), hasNext);
    }

    private BooleanExpression notificationIdLt(Long cursor) {
        return cursor == null ? null : notification.id.lt(cursor);
    }
}
