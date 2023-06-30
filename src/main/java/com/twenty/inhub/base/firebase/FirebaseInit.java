package com.twenty.inhub.base.firebase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Service
public class FirebaseInit {

    private static final String outputPath = "src/main/resources/";
    private static final String path = "firebase/inhub-50c3d-firebase-adminsdk-cgp8c-3b0879410e.json";
    @Value("${custom.firebase.project_id}")
    private String project_id;
    @Value("${custom.firebase.private_key_id}")
    private String private_key_id;
    @Value("${custom.firebase.private_key}")
    private String private_key;
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
//            HashMap<String, Object> config = new HashMap<>();
//
//            config.put("type", type);
//            config.put("project_id", project_id);
//            config.put("private_key_id", private_key_id);
//            config.put("private_key", private_key);
//            config.put("client_email", client_email);
//            config.put("client_id", client_id);
//            config.put("auth_uri", auth_uri);
//            config.put("token_uri", token_uri);
//            config.put("auth_provider_x509_cert_url", auth_provider_x509_cert_url);
//            config.put("client_x509_cert_url", client_x509_cert_url);
//            config.put("universe_domain", universe_domain);
//
//            ObjectMapper mapper = new ObjectMapper();
//            File file = new File(outputPath + path);
//            mapper.writeValue(file, config);


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
