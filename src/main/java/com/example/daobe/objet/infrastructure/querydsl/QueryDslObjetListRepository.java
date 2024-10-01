package com.example.daobe.objet.infrastructure.querydsl;

import static com.example.daobe.objet.domain.QObjet.objet;
import static com.example.daobe.objet.domain.QObjetSharer.objetSharer;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetStatus;
import com.example.daobe.objet.domain.repository.ObjetListRepository;
import com.example.daobe.objet.domain.repository.dto.ObjetListCondition;
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
public class QueryDslObjetListRepository implements ObjetListRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Objet> getObjetListOfSharerByCondition(ObjetListCondition condition) {
        List<Objet> result = queryFactory.selectFrom(objet)
                .join(objetSharer).on(objetSharer.objet.eq(objet))
                .where(
                        objetIdLt(condition.cursor()),
                        objet.lounge.id.eq(condition.loungeId()),
                        objetSharer.user.id.eq(condition.userId()),
                        objet.status.eq(ObjetStatus.ACTIVE)
                )
                .orderBy(objet.id.desc())
                .limit(condition.executeLimitSize())
                .fetch();

        boolean hasNext = isHasNext(condition, result);

        return new SliceImpl<>(result, PageRequest.ofSize(condition.executeLimitSize()), hasNext);
    }

    @Override
    public Slice<Objet> getObjetListByCondition(ObjetListCondition condition) {
        List<Objet> result = queryFactory.selectFrom(objet)
                .where(
                        objetIdLt(condition.cursor()),
                        objet.lounge.id.eq(condition.loungeId()),
                        objet.status.eq(ObjetStatus.ACTIVE)
                )
                .orderBy(objet.id.desc())
                .limit(condition.executeLimitSize())
                .fetch();

        boolean hasNext = isHasNext(condition, result);

        return new SliceImpl<>(result, PageRequest.ofSize(condition.executeLimitSize()), hasNext);
    }

    private static boolean isHasNext(ObjetListCondition condition, List<Objet> result) {
        boolean hasNext = false;
        if (result.size() == condition.executeLimitSize()) {
            result.remove(result.size() - 1);
            hasNext = true;
        }
        return hasNext;
    }

    private BooleanExpression objetIdLt(Long cursor) {
        return cursor == null ? null : objet.id.lt(cursor);
    }
}
