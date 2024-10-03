package com.example.daobe.objet.infrastructure.querydsl;

import static com.example.daobe.objet.domain.QObjet.objet;
import static com.example.daobe.objet.domain.QObjetSharer.objetSharer;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharerStatus;
import com.example.daobe.objet.domain.ObjetStatus;
import com.example.daobe.objet.domain.repository.CustomObjetRepository;
import com.example.daobe.objet.domain.repository.dto.ObjetFindCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslCustomObjetRepository implements CustomObjetRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<Objet> getObjetListByCondition(ObjetFindCondition condition) {
        JPAQuery<Objet> query = queryFactory.selectFrom(objet)
                .where(
                        objetIdLt(condition.cursor()),
                        objet.lounge.id.eq(condition.loungeId()),
                        objet.status.eq(ObjetStatus.ACTIVE)
                )
                .orderBy(objet.id.desc())
                .limit(condition.executeLimitSize());

        List<Objet> result = (condition.userId() != null)
                ? query.join(objetSharer).on(objetSharer.objet.eq(objet))
                .where(
                        objetSharer.user.id.eq(condition.userId()),
                        objetSharer.status.eq(ObjetSharerStatus.ACTIVE)
                ).fetch()
                : query.fetch();

        boolean hasNext = false;
        if (result.size() == condition.executeLimitSize()) {
            result.remove(result.size() - 1);
            hasNext = true;
        }
        return new SliceImpl<>(result, PageRequest.ofSize(condition.executeLimitSize()), hasNext);
    }

    private BooleanExpression objetIdLt(Long cursor) {
        return cursor == null ? null : objet.id.lt(cursor);
    }
}
