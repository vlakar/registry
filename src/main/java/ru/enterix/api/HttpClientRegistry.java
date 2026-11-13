package ru.enterix.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface HttpClientRegistry {

    void replaceSubscribers(@Valid Subscribers subscribers);

    @Valid
    Subscriber getSubscriber(@NotNull String baseUrl, @NotNull String phone);
}
