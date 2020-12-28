package me.udintsev.otus.architect.eventing.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.udintsev.otus.architect.eventing.order.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@AllArgsConstructor
@Component
@Slf4j
public class OrderStatusChangeListener implements Consumer<Order> {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void accept(Order order) {
        log.info("Order status changed: id={}, newStatus={}",
                order.getId(), order.getStatus());

        var persistedOrder = orderRepository.getOne(order.getId());
        persistedOrder.setStatus(order.getStatus());
    }
}
