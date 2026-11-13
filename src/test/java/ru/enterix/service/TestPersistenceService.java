package ru.enterix.service;

import org.springframework.stereotype.Component;
import ru.enterix.api.Subscriber;
import ru.enterix.api.Subscribers;
import ru.enterix.controller.BadSubscribersException;
import ru.enterix.controller.SubscriberNotFoundException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Наивная имплементация, которая каждый раз считывает файл целиком, чтобы найти нужного подписчика.
 * <p>
 * Сразу кладу её в папку тест, чтоб показать, что в рабочем коде её использовать нельзя.
 */
@Component
public class TestPersistenceService implements PersistenceService {

    private final ObjectWriter objectWriter;
    private final ObjectReader objectReader;
    private final Path storageFile;

    {
        try {
            storageFile =
                    Files.createTempFile("subscribers", ".json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        objectReader = objectMapper.readerFor(Subscribers.class);
    }

    @Override
    public void replace(Subscribers subscribers) {
        validate(subscribers);
        objectWriter.writeValue(storageFile, subscribers);
    }

    private void validate(Subscribers subscribers) {
        final long count = subscribers.subscribers().stream().map(Subscriber::id).distinct().count();
        if (count != subscribers.subscribers().size()) {
            throw new BadSubscribersException("Duplicate subscriber ids found");
        }
    }

    @Override
    public Subscriber findByBaseUrlAndPhone(String baseUrl, String phone) {
        try {
            if (!Files.isReadable(storageFile) || Files.size(storageFile) == 0) {
                throw new SubscriberNotFoundException(baseUrl, phone);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ((Subscribers) objectReader.readValue(storageFile))
                .subscribers()
                .stream()
                .filter(s -> s.baseUrl().equals(baseUrl) && s.phone().equals(phone))
                .findFirst()
                .orElseThrow(() -> new SubscriberNotFoundException(baseUrl, phone));
    }

    public void spoilData() {
        objectWriter.writeValue(storageFile, "non json content");
    }
}
