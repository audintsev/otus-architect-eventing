package me.udintsev.otus.architect.eventing.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.udintsev.otus.architect.eventing.notification.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NotificationApplicationTests {
    private static final Instant NOTIF1_SENT_AT = Instant.now();
    private static final Instant NOTIF2_SENT_AT = NOTIF1_SENT_AT.plus(10, ChronoUnit.SECONDS);
    private static final Instant NOTIF3_SENT_AT = NOTIF1_SENT_AT.plus(30, ChronoUnit.SECONDS);

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        notificationRepository.save(new Notification(
                null,
                "john.doe@example.com",
                NOTIF1_SENT_AT,
                "msg1"
        ));
        notificationRepository.save(new Notification(
                null,
                "john.doe@example.com",
                NOTIF3_SENT_AT,
                "msg3"
        ));
        notificationRepository.save(new Notification(
                null,
                "jane.doe@example.com",
                NOTIF2_SENT_AT,
                "msg2"
        ));
    }

    @Test
    void returnsUserNotifications() throws Exception {
        mvc.perform(
                get(NotificationController.API_ROOT)
                        .header("X-User-Id", "john.doe@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].sentAt").value(NOTIF1_SENT_AT.toString()))
                .andExpect(jsonPath("$[0].message").value("msg1"))
                .andExpect(jsonPath("$[1].userId").value("john.doe@example.com"))
                .andExpect(jsonPath("$[1].sentAt").value(NOTIF3_SENT_AT.toString()))
                .andExpect(jsonPath("$[1].message").value("msg3"))
        ;

        mvc.perform(
                get(NotificationController.API_ROOT)
                        .header("X-User-Id", "jane.doe@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value("jane.doe@example.com"))
                .andExpect(jsonPath("$[0].sentAt").value(NOTIF2_SENT_AT.toString()))
                .andExpect(jsonPath("$[0].message").value("msg2"))
        ;
    }

    @Test
    void returnsAllNotifications() throws Exception {
        mvc.perform(
                get(NotificationController.API_ROOT)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].userId").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].sentAt").value(NOTIF1_SENT_AT.toString()))
                .andExpect(jsonPath("$[0].message").value("msg1"))
                .andExpect(jsonPath("$[1].userId").value("jane.doe@example.com"))
                .andExpect(jsonPath("$[1].sentAt").value(NOTIF2_SENT_AT.toString()))
                .andExpect(jsonPath("$[1].message").value("msg2"))
                .andExpect(jsonPath("$[2].userId").value("john.doe@example.com"))
                .andExpect(jsonPath("$[2].sentAt").value(NOTIF3_SENT_AT.toString()))
                .andExpect(jsonPath("$[2].message").value("msg3"))
        ;
    }
}
