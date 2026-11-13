package ru.enterix.controller;

public class BadSubscribersException extends RuntimeException {
    public BadSubscribersException(String message) {
        super(message);
    }
}
