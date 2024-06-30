package org.codingtest.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codingtest.domain.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenGenerator {

    private final SecretKeyProvider secretKeyProvider;

    public Map<String, Object> generateToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new BadCredentialsException("Invalid Principal User Type");
        }
        SecretKey key = this.secretKeyProvider.getSecretKey();
        JWSHeader header = new JWSHeader(this.secretKeyProvider.getAlgorithm());
        JWTClaimsSet claimsSet = buildClaimsSet(user);
        return getClaimSet(header, claimsSet, key);
    }

    private static Map<String, Object> getClaimSet(JWSHeader header, JWTClaimsSet claimsSet, SecretKey key) {
        SignedJWT jwt = new SignedJWT(header, claimsSet);

        Map<String, Object> map = new HashMap<>();

        try {
            JWSSigner signer = new MACSigner(key);
            jwt.sign(signer);
            map.put("token", jwt.serialize());
            map.put("expires_in", jwt.getJWTClaimsSet().getExpirationTime().getTime());
        } catch (JOSEException e) {
            throw new RuntimeException("Unable to generate JWT", e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private JWTClaimsSet buildClaimsSet(User user) {
        Instant issuedAt = Instant.now();
        var expirationTime = issuedAt.plus(1, ChronoUnit.DAYS);

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer(user.getId().toString())
                .issueTime(Date.from(issuedAt))
                .expirationTime(Date.from(expirationTime))
                .subject(user.getEmail())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("first_name", user.getFirstName())
                .claim("last_name", user.getLastName());
        return builder.build();
    }

}
