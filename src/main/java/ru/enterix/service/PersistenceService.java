package ru.enterix.service;

import ru.enterix.api.Subscriber;
import ru.enterix.api.Subscribers;

interface PersistenceService {
    void replace(Subscribers subscribers);

    Subscriber findByBaseUrlAndPhone(String baseUrl, String phone);
}
