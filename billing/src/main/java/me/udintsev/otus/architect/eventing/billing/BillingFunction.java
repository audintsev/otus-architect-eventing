package me.udintsev.otus.architect.eventing.billing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.udintsev.otus.architect.eventing.billing.domain.Order;
import me.udintsev.otus.architect.eventing.billing.domain.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Component
@AllArgsConstructor
@Slf4j
public class BillingFunction implements Function<Order, Optional<Order>> {

    private final AccountRepository accountRepository;

    @Transactional
    public Optional<Order> apply(Order order) {
        log.info("Received order: {}", order);

        if (order.getStatus() != OrderStatus.CHECKED_OUT) {
            return Optional.empty();
        }

        var result = accountRepository.findById(order.getUserId())
                .map(account -> {
                    if (account.getAmount() >= order.getPrice()) {
                        account.setAmount(account.getAmount() - order.getPrice());
                        order.setStatus(OrderStatus.PAYMENT_SUCCEEDED);
                        log.info("Payment succeeded for order {}", order);
                    } else {
                        order.setStatus(OrderStatus.PAYMENT_FAILED);
                        log.info("Payment failed for order {}", order);
                    }
                    return order;
                })
                .orElseGet(() -> {
                    log.warn("Account {} was not found", order.getUserId());
                    order.setStatus(OrderStatus.PAYMENT_FAILED);
                    return order;
                });
        return Optional.of(result);
    }
}
