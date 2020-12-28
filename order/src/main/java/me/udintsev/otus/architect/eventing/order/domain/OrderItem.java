package me.udintsev.otus.architect.eventing.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Data
@Embeddable
@Table(name = "ORDER_ITEMS")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Column(name = "ITEM_ID")
    private long itemId;

    @Column(name = "QUANTITY")
    private int quantity;
}
