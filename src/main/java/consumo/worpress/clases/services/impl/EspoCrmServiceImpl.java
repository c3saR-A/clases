package consumo.worpress.clases.services.impl;


import consumo.worpress.clases.services.EspoCrmService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EspoCrmServiceImpl implements EspoCrmService {
    private final WebClient webClient;

    public EspoCrmServiceImpl(@Qualifier("espoCrmWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Map<String, Object> createLead(String firstName, String lastName, String email, String description) {
        return webClient.post()
                .uri("/api/v1/Lead")
                .bodyValue(Map.of(
                        "firstName", firstName,
                        "lastName", lastName,
                        "status", "New",
                        "source", "Web Site",
                        "description", description,
                        "emailAddress", email
                ))
                .retrieve()
                .onStatus(s -> s.is4xxClientError(),
                        r -> r.bodyToMono(String.class)
                                .map(err -> new RuntimeException("Error EspoCRM: " +
                                        err)))
                .bodyToMono(Map.class)
                .block();

    }

    @Override
    public Map<String, Object> createContact(String firstName, String lastName, String email) {
        return webClient.post()
                .uri("/api/v1/Contact") // Cambiamos el endpoint a Contact
                .bodyValue(Map.of(
                        "firstName", firstName,
                        "lastName", lastName,
                        "emailAddress", email
                ))
                .retrieve()
                .onStatus(s -> s.is4xxClientError(),
                        r -> r.bodyToMono(String.class)
                                .map(err -> new RuntimeException("Error creando Contacto en EspoCRM: " + err)))
                .bodyToMono(Map.class)
                .block();
    }
}
