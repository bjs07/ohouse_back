package com.clone.ohouse.shop.store.domain.access;

import com.clone.ohouse.shop.product.domain.access.CategorySearch;
import com.clone.ohouse.shop.product.domain.entity.Product;
import com.clone.ohouse.shop.product.domain.entity.QItem;
import com.clone.ohouse.shop.product.domain.entity.QItemCategory;
import com.clone.ohouse.shop.product.domain.entity.QProduct;
import com.clone.ohouse.shop.store.domain.dto.BundleVIewDto;
import com.clone.ohouse.shop.store.domain.entity.QStorePosts;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.clone.ohouse.shop.product.domain.entity.QItem.*;
import static com.clone.ohouse.shop.product.domain.entity.QItemCategory.*;
import static com.clone.ohouse.shop.product.domain.entity.QProduct.*;
import static com.clone.ohouse.shop.store.domain.entity.QStorePosts.*;


@RequiredArgsConstructor
public class StorePostsRepositoryImpl implements StorePostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StorePosts> getBundleViewByCategoryWithConditionV1(Long categoryId, Pageable pageable) {
    return queryFactory
                .select(storePosts)
                .from(storePosts)
                .where(storePosts.id.in(
                        JPAExpressions
                                .select(product.storePosts.id)
                                .from(product)
                                .join(product.item, item)
                                .join(item.itemCategories, itemCategory)
                                .where(itemCategory.category.id.eq(categoryId))
                                .orderBy(product.popular.desc())
                        ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
//        QStorePosts qStorePosts = new QStorePosts("Q");
//        QProduct qProduct = new QProduct("P");
//        return queryFactory
//                .select(storePosts)
//                .from(storePosts)
//                .where(storePosts.id.in(JPAExpressions
//                        .select(qStorePosts.id)
//                        .from(qProduct)
//                        .join(qProduct.storePosts, qStorePosts)
//                        .where(qStorePosts.id.in(1L, 2L, 3L))))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
    }

    @Override
    public List<StorePosts> getBundleViewByCategoryWithConditionV2(Long categoryId, Pageable pageable) {
        return queryFactory
                .select(storePosts).distinct()
                .from(product)
                .join(product.storePosts, storePosts)
                .join(product.item, item)
                .join(item.itemCategories, itemCategory)
                .where(itemCategory.category.id.eq(categoryId).and(product.popular.gt(50)))
//                .where(itemCategory.category.id.eq(categoryId))
                .orderBy(product.popular.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public BundleVIewDto getBundleViewByCategoryWithConditionV3(Long categoryId, Pageable pageable) {
        //Total Count
        // 성능에 우려가 있지만 현재로선 별 다른 방법이 없음, 일대다 관계에서 groupby 이후에 재 계산될 수 있는 count쿼리가 필요함
        Long count = queryFactory
                .select(storePosts.count()).distinct()
                .from(storePosts)
                .where(storePosts.id.in(
                        JPAExpressions
                                .select(product.storePosts.id)
                                .from(product)
                                .join(product.item, item)
                                .join(item.itemCategories, itemCategory)
                                .where(itemCategory.category.id.eq(categoryId))
                ))
                .fetchOne();


        List<Tuple> tuples = queryFactory
                .select(storePosts,
                        product.popular.sum()).distinct()
                .offset(0)
                .from(storePosts)
                .join(storePosts.productList, product)
                .join(product.item, item)
                .join(item.itemCategories, itemCategory)
//                .where(itemCategory.category.id.eq(categoryId).and(storePosts.isActive.eq(true)).and(storePosts.isDeleted.eq(false)))
                .where(itemCategory.category.id.eq(categoryId))
                .groupBy(storePosts.id)
                .orderBy(product.popular.sum().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<StorePosts> posts = new ArrayList<>();
        List<Long> postIds = new ArrayList<>();
        for (Tuple tuple : tuples) {
            StorePosts post = tuple.get(storePosts);
            postIds.add(post.getId());
            posts.add(post);
            System.out.println("product.sum = " + tuple.get(product.popular.sum()).toString());
        }

        Map<Long, Product> productMap = queryFactory
                .select(storePosts.id, product)
                .from(product)
                .join(product.storePosts, storePosts)
                .where(storePosts.id.in(postIds))
                .orderBy(product.id.asc())
                .transform(GroupBy.groupBy(storePosts.id).as(product));

        //combine
        List<StorePostsViewDto> views = new ArrayList<>();
        for (StorePosts post : posts) {
            Product product = productMap.get(post.getId());
            views.add(new StorePostsViewDto(post, product.getPrice(), product.getRateDiscount(), product.getPopular()));
        }

        BundleVIewDto result = new BundleVIewDto(count, views.size(), views);

        return result;
    }
}
