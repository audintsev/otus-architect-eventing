package me.udintsev.otus.architect.eventing.order;

import me.udintsev.otus.architect.eventing.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
