package me.udintsev.otus.architect.eventing.billing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNTS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private long amount;
}
