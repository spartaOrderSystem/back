package com.spartaordersystem.domains.storeMenu.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.storeMenu.enums.MenuStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.spartaordersystem.domains.storeMenu.entity.QStoreMenu.storeMenu;

@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreMenu> getMenusBySearchOptions(Pageable pageable, String keyword, Long maxPrice, Long minPrice, MenuStatus menuStatus) {
        JPQLQuery<StoreMenu> query = queryFactory.selectFrom(storeMenu)
                .where(containsKeyword(keyword), priceMinRange(minPrice), priceMaxRange(maxPrice), eqMenuStatus(menuStatus))
                .offset(pageable.getOffset()) // 페이지 번호 - 0부터 시작
                .limit(pageable.getPageSize()) // 페이지 사이즈
                .orderBy(storeMenu.createdAt.desc(), storeMenu.updatedAt.desc()); // 앞 조건 먼저 정렬 적용. 생성일 순 정렬 후 수정일 순 정렬됨

        List<StoreMenu> stores = query.fetch();
        return new PageImpl<StoreMenu>(stores, pageable, query.fetchCount());
    }


    private BooleanExpression eqMenuStatus(MenuStatus menuStatus) {
        if (menuStatus == null) {
            return null;
        }
        return storeMenu.menuStatus.eq(menuStatus);
    }

    private BooleanExpression priceMinRange(Long minPrice) {
        if (minPrice == null) {
            return null;
        }
        return storeMenu.price.loe(minPrice);
    }

    private BooleanExpression priceMaxRange(Long maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return storeMenu.price.goe(maxPrice);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        return storeMenu.title.containsIgnoreCase(keyword).or(storeMenu.description.containsIgnoreCase(keyword));
    }
}
