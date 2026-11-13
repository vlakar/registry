package ru.enterix.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.enterix.service.RegistryService;
import ru.enterix.service.TestPersistenceService;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class HttpClientRegistryControllerTest {

    private HttpClientRegistryController controller;

    @BeforeEach
    void setUp() {
        controller = new HttpClientRegistryController(new RegistryService(new TestPersistenceService()));
    }

    @Test
    void notFoundOnEmpty() throws Exception {
        standaloneSetup(controller)
                .setControllerAdvice(new RegistryExceptionHandler())
                .build()
                .perform(get("/perm/subscribers/79111111111")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void postValidNewList() throws Exception {

        standaloneSetup(controller).build()
                .perform(put("/subscribers")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"subscribers": [{"id":"1","baseUrl":"perm","apiKey":"9d32da17-d17e-470f-acd8-e592ccd866a0","phone":"79111111111"}]}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void postDuplicatesById() throws Exception {

        standaloneSetup(controller)
                .setControllerAdvice(new RegistryExceptionHandler())
                .build()
                .perform(put("/subscribers")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"subscribers": [
                                    {"id":"1","baseUrl":"perm","apiKey":"9d32da17-d17e-470f-acd8-e592ccd866a0","phone":"79111111111"},
                                    {"id":"1","baseUrl":"ufa","apiKey":"9d32da17-d17e-470f-acd8-e592ccd866a0","phone":"79222222222"}
                                ]}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Nested
    class QueryingRegistry {

        private final UUID firstId = UUID.randomUUID();
        private final UUID secondId = UUID.randomUUID();

        @BeforeEach
        void setUp() throws Exception {
            standaloneSetup(controller)
                    .build()
                    .perform(put("/subscribers")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "subscribers" : [
                                            {"id": "1", "baseUrl": "perm", "apiKey": "%s", "phone": "79111111111"},
                                            {"id": "2", "baseUrl": "ufa", "apiKey": "%s", "phone": "79222222222"}
                                        ]
                                    }
                                    """.formatted(firstId, secondId)))
                    .andExpect(status().isOk());
        }

        @Test
        void fetchFirst() throws Exception {
            standaloneSetup(controller)
                    .setControllerAdvice(new RegistryExceptionHandler())
                    .build()
                    .perform(get("/perm/subscribers/79111111111")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            {"id": "1", "baseUrl": "perm", "apiKey": "%s", "phone": "79111111111"}
                            """.formatted(firstId)));
        }

        @Test
        void fetchSecond() throws Exception {
            standaloneSetup(controller)
                    .setControllerAdvice(new RegistryExceptionHandler())
                    .build()
                    .perform(get("/ufa/subscribers/79222222222")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andExpect(content().json("""
                            {"id": "2", "baseUrl": "ufa", "apiKey": "%s", "phone": "79222222222"}
                            """.formatted(secondId)));
        }

        @Test
        void fetchNonExistent() throws Exception {
            standaloneSetup(controller)
                    .setControllerAdvice(new RegistryExceptionHandler())
                    .build()
                    .perform(get("/samara/subscribers/79333333333")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}