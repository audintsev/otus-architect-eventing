package me.udintsev.otus.architect.eventing.notification.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum OrderStatus {
    CREATED,
    CHECKED_OUT,
    PAYMENT_SUCCEEDED,
    PAYMENT_FAILED;

    @JsonValue
    public String getJsonValue() {
        return this.name().toLowerCase(Locale.ENGLISH);
    }
}
