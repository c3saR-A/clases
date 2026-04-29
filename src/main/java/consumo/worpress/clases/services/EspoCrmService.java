package consumo.worpress.clases.services;

import java.util.Map;

public interface EspoCrmService {

    Map<String, Object> createLead(String firstName, String lastName,
                                   String email, String description);
    Map<String, Object> createContact(String firstName, String lastName, String email);

}
