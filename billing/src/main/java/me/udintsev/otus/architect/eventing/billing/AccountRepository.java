package me.udintsev.otus.architect.eventing.billing;

import me.udintsev.otus.architect.eventing.billing.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
