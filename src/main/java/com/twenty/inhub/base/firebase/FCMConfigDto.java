package com.twenty.inhub.base.firebase;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class FCMConfigDto {
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
}
