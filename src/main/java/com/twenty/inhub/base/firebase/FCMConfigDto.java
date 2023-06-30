package com.twenty.inhub.base.firebase;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class FCMConfigDto {
    @Value("${custom.firebase.project_id}")
    private final String project_id;
    @Value("${custom.firebase.private_key_id}")
    private final String private_key_id;
    @Value("${custom.firebase.private_key}")
    private final String private_key;
    @Value("${custom.firebase.client_email}")
    private final String client_email;
    @Value("${custom.firebase.client_id}")
    private final String client_id;
}
