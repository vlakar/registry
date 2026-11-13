package ru.enterix.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.enterix.api.HttpClientRegistry;
import ru.enterix.api.Subscriber;
import ru.enterix.api.Subscribers;
import ru.enterix.service.RegistryService;

@RestController
@RequiredArgsConstructor
public class HttpClientRegistryController implements HttpClientRegistry {

    private final RegistryService registryService;

    @Override
    @PutMapping(path = "/subscribers", consumes = "application/json")
    public void replaceSubscribers(@RequestBody Subscribers subscribers) {
        registryService.replaceSubscribers(subscribers);
    }

    @GetMapping(path = "/{baseUrl}/subscribers/{phone}", produces = "application/json")
    @Override
    public Subscriber getSubscriber(@PathVariable(name = "baseUrl") String baseUrl,
                                    @PathVariable(name = "phone") String phone) {
        return registryService.getSubscriber(baseUrl, phone);
    }

}
