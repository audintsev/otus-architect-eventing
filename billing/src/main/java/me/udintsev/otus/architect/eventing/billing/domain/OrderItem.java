package me.udintsev.otus.architect.eventing.billing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private long itemId;
    private int quantity;
}
