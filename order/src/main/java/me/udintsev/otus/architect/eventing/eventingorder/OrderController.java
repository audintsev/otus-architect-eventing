package me.udintsev.otus.architect.eventing.eventingorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping(OrderController.API_ROOT)
@Transactional(readOnly = true)
public class OrderController {
    public static final String API_ROOT = "/api/v1/orders";

    private final OrderRepository orderRepository;

    @GetMapping
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOrderRequest {
        List<OrderItem> items;
    }

    @PostMapping
    @Transactional
    public Order createOrder(@RequestHeader("X-User-Id") String userId,
                             @RequestBody CreateOrderRequest createOrderRequest) {
        Order newOrder = new Order(
                null,
                userId,
                createOrderRequest.getItems(),
                OrderStatus.CREATED
        );
        return orderRepository.save(newOrder);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable long orderId) {
        return ResponseEntity.of(orderRepository.findById(orderId));
    }

    @PostMapping("{orderId}/checkout")
    @Transactional
    public ResponseEntity<Order> checkoutOrder(@PathVariable long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(OrderStatus.CHECKED_OUT);
                    return orderRepository.save(order);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
