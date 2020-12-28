package me.udintsev.otus.architect.eventing.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.udintsev.otus.architect.eventing.billing.domain.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AccountController.API_ROOT)
@AllArgsConstructor
@Transactional(readOnly = true)
public class AccountController {
    public static final String API_ROOT = "/api/v1/billing";

    private final AccountRepository accountRepository;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        long amount;
    }

    @GetMapping
    public ResponseEntity<Account> get(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.of(accountRepository.findById(userId));
    }

    @PostMapping("deposit")
    @Transactional
    public Account depositMoney(@RequestHeader("X-User-Id") String userId,
                                @RequestBody Request request) {
        Assert.isTrue(request.getAmount() > 0, "amount must be positive");

        var account = accountRepository.findById(userId)
                .orElseGet(() -> new Account(userId, 0));

        account.setAmount(account.getAmount() + request.getAmount());

        return accountRepository.save(account);
    }
}
