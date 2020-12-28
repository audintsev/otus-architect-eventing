package me.udintsev.otus.architect.eventing.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "NOTIFICATIONS",
        indexes = {
                @Index(columnList = "userId, sentAt"),
                @Index(columnList = "sentAt")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Instant sentAt;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String message;
}
