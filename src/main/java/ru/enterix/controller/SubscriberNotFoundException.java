package ru.enterix.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubscriberNotFoundException extends RuntimeException {
    private final String baseUrl;
    private final String phone;
}
