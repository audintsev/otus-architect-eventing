package me.udintsev.otus.architect.eventing.notification;

import lombok.AllArgsConstructor;
import me.udintsev.otus.architect.eventing.notification.domain.Notification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(NotificationController.API_ROOT)
@AllArgsConstructor
@Transactional(readOnly = true)
public class NotificationController {
    public static final String API_ROOT = "/api/v1/notification";

    private final NotificationRepository notificationRepository;

    @GetMapping
    public List<Notification> get(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        return userId == null
                ? notificationRepository.findByOrderBySentAt()
                : notificationRepository.findByUserIdOrderBySentAt(userId);
    }
}
