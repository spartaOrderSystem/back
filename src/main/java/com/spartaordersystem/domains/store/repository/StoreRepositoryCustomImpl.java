package com.spartaordersystem.domains.store.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.enums.StoreStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.spartaordersystem.domains.store.entity.QStore.store;


public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public StoreRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public Page<Store> getStoresBySearchOptions(Pageable pageable, String title, StoreStatus status, Category category) {
        JPQLQuery<Store> query = queryFactory.selectFrom(store)
                .where(containsTitle(title), eqStatus(status), eqCategory(category))
                .offset(pageable.getOffset()) // 페이지 번호 - 0부터 시작
                .limit(pageable.getPageSize()) // 페이지 사이즈
                .orderBy(store.createdAt.desc());
        List<Store> stores = query.fetch();
        return new PageImpl<Store>(stores, pageable, query.fetchCount());


    }

    private BooleanExpression containsTitle(String title) {
        if (title == null) {
            return null;
        }
        return store.title.containsIgnoreCase(title);
    }

    private BooleanExpression eqCategory(Category category) {
        if (category == null) {
            return null;
        }
        return store.category.eq(category);
    }

    private BooleanExpression eqStatus(StoreStatus status) {
        if (status == null) { // || status.isBlank()
            return null;
        }
        return store.storeStatus.eq(status);
    }
}
