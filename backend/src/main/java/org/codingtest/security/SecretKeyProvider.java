package org.codingtest.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class SecretKeyProvider {

    @Value("${access-token.secret-key}")
    private String secretString;
    private SecretKey secretKey;
    @Getter
    private final JWSAlgorithm algorithm = JWSAlgorithm.HS256;

    public SecretKey getSecretKey() {
        if (this.secretKey == null) {
            OctetSequenceKey jwk = new OctetSequenceKey.Builder(secretString.getBytes())
                    .algorithm(algorithm)
                    .build();
            this.secretKey = jwk.toSecretKey();
        }
        return secretKey;
    }

}