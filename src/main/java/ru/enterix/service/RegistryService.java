package ru.enterix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.enterix.api.Subscriber;
import ru.enterix.api.Subscribers;

@Component
@RequiredArgsConstructor
public class RegistryService {

    private final PersistenceService persistenceService;

    public Subscriber getSubscriber(String baseUrl, String phone) {
        return persistenceService.findByBaseUrlAndPhone(baseUrl, phone);
    }

    public void replaceSubscribers(Subscribers subscribers) {
        persistenceService.replace(subscribers);
    }
}
