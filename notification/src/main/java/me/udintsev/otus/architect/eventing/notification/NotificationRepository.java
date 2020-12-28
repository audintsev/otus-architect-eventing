package me.udintsev.otus.architect.eventing.notification;

import me.udintsev.otus.architect.eventing.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOrderBySentAt();
    List<Notification> findByUserIdOrderBySentAt(String userId);
}
