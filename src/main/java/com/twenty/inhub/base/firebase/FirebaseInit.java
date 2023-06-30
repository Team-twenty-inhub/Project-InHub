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

    private static final String outputPath = "src/main/resources/";
    private static final String path = "firebase/inhub-50c3d-firebase-adminsdk-cgp8c-3b0879410e.json";
    @Value("${custom.firebase.project_id}")
    private String project_id;
    @Value("${custom.firebase.private_key_id}")
    private String private_key_id;
    @Value("${custom.firebase.client_email}")
    private String client_email;
    @Value("${custom.firebase.client_id}")
    private String client_id;
    @Value("${custom.firebase.type}")
    private String type;
    @Value("${custom.firebase.auth_uri}")
    private String auth_uri;
    @Value("${custom.firebase.token_uri}")
    private String token_uri;
    @Value("${custom.firebase.auth_provider_x509_cert_url}")
    private String auth_provider_x509_cert_url;
    @Value("${custom.firebase.client_x509_cert_url}")
    private String client_x509_cert_url;
    @Value("${custom.firebase.universe_domain}")
    private String universe_domain;

    @PostConstruct
    public void init() {

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(path).getFile());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(file);

            ((ObjectNode) node).put("type", project_id);
            ((ObjectNode) node).put("project_id", type);
            ((ObjectNode) node).put("private_key_id", project_id);
            ((ObjectNode) node).put("client_email", private_key_id);
            ((ObjectNode) node).put("client_id", client_email);
            ((ObjectNode) node).put("auth_uri", client_id);
            ((ObjectNode) node).put("token_uri", auth_uri);
            ((ObjectNode) node).put("auth_provider_x509_cert_url", token_uri);
            ((ObjectNode) node).put("client_x509_cert_url", auth_provider_x509_cert_url);
            ((ObjectNode) node).put("universe_domain", universe_domain);


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
