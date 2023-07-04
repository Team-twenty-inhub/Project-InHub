package com.twenty.inhub.base.firebase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class FirebaseInit {

    private static final String path = "firebase/inhub-50c3d-firebase-adminsdk-cgp8c-3b0879410e.json";
    @Value("${custom.firebase.project_id}")
    private String project_id;
    @Value("${custom.firebase.private_key_id}")
    private String private_key_id;
    @Value("${custom.firebase.client_email}")
    private String client_email;
    @Value("${custom.firebase.type}")
    private String type;

    @PostConstruct
    public void init() {

        FCMConfigDto fcmConfigDto = new FCMConfigDto();

        String authUri = fcmConfigDto.getAuth_uri();

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(path).getFile());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(file);

            ((ObjectNode) node).put("project_id", type);
            ((ObjectNode) node).put("private_key_id", project_id);
            ((ObjectNode) node).put("client_email", private_key_id);
            ((ObjectNode) node).put("client_id", client_email);

            objectMapper.writeValue(file, node);


            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(
                            new ClassPathResource(path)
                                    .getInputStream()
                    )).build();

            if (FirebaseApp.getApps().isEmpty())
                FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
