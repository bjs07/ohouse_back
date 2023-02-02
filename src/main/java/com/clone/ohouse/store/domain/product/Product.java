package com.clone.ohouse.store.domain.product;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.clone.ohouse.store.domain.order.Order;
import com.clone.ohouse.store.domain.order.OrderedProduct;
import com.clone.ohouse.error.order.OrderError;
import com.clone.ohouse.error.order.OrderFailException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(length = 45)
    private String productName;
    private Long price;
    private Long stock;
    private Integer rateDiscount;

    private Long popular = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_posts_id")
    private StorePosts storePosts;

    @Builder
    public Product(Item item, String productName, Long price, Long stock, Integer rateDiscount, Long popular, StorePosts storePosts) {
        this.item = item;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
        this.popular = popular;
        this.storePosts = storePosts;
    }

    public void GainPopular() {
        this.popular++;
    }

    public void update(Item item, String productName, Long stock, Long price, Integer rateDiscount,StorePosts storePosts) {
        if(item != null) this.item = item;
        if(productName != null) this.productName = productName;
        if(price != null) this.price = price;
        if(stock != null) this.stock = stock;
        if(rateDiscount != null) this.rateDiscount = rateDiscount;
        if(storePosts != null) this.storePosts = storePosts;


    }

    public void returnAmount(Long count) throws Exception{
        if(count <= 0L) throw new RuntimeException("잘못된 count 인수 : " + count);
        this.stock += count;
    }

    public OrderedProduct makeOrderedProduct(Order order, Long price, Long amount) throws Exception{
        if(amount > this.stock) throw new OrderFailException("재고보다 많이 주문할 수 없습니다", OrderError.NO_ENOUGH_STOCK);

        this.stock -= amount;

        return new OrderedProduct(this, order, price, amount);
    }

    public void registerStorePosts(StorePosts post){
        this.storePosts = post;
        post.getProductList().add(this);
    }

}
