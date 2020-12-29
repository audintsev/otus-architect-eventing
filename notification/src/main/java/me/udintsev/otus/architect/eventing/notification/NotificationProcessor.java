package me.udintsev.otus.architect.eventing.notification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.udintsev.otus.architect.eventing.notification.domain.Notification;
import me.udintsev.otus.architect.eventing.notification.domain.Order;
import me.udintsev.otus.architect.eventing.notification.domain.OrderItem;
import me.udintsev.otus.architect.eventing.notification.domain.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationProcessor implements Consumer<Order> {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void accept(Order order) {
        log.info("Notifying about order: {}", order);

        String message = order.getStatus() == OrderStatus.PAYMENT_SUCCEEDED ? "YES!!! Your order payment has succeeded!"
                : order.getStatus() == OrderStatus.PAYMENT_FAILED ? "Ohh... no.... Your order payment has failed :("
                : null;

        if (message == null) {
            return;
        }

        message += " Supplementary info: orderId=" + order.getId() + ", total: " + order.getPrice()
                + ", number of articles: " + Optional.ofNullable(order.getItems()).map(List::size).orElse(0)
                + ", number of items: " + Optional.ofNullable(order.getItems()).stream().flatMap(Collection::stream).mapToInt(OrderItem::getQuantity).sum();

        notificationRepository.save(new Notification(
                null,
                order.getUserId(),
                Instant.now(),
                message
        ));
    }
}
