package ru.enterix.api;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record Subscribers(@NotNull List<Subscriber> subscribers) {
}
