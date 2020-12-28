package me.udintsev.otus.architect.eventing.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String userId;
    private List<OrderItem> items;
    private OrderStatus status;
    private long price;
}
