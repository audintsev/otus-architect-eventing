package me.udintsev.otus.architect.eventing.order;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum OrderStatus {
    CREATED,
    CHECKED_OUT,
    PAYMENT_SUCCEEDED,
    PAYMENT_FAILED,
    FULFILLED;

    @JsonValue
    public String getJsonValue() {
        return this.name().toLowerCase(Locale.ENGLISH);
    }
}