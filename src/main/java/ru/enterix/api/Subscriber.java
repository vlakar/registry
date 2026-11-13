package ru.enterix.api;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record Subscriber(@NotEmpty String id, @NotEmpty String baseUrl, @NotNull UUID apiKey, @NotEmpty String phone) {
}
