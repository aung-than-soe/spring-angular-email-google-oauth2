package org.codingtest.config;

import lombok.extern.slf4j.Slf4j;
import org.codingtest.dto.GoogleUserInfo;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final WebClient googleWebClient;

    public GoogleOpaqueTokenIntrospector(WebClient googleWebClient) {
        this.googleWebClient = googleWebClient;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        GoogleUserInfo userInfo = googleWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/oauth2/v3/userinfo").queryParam("alt", "json").queryParam("access_token", token).build())
                .retrieve()
                .bodyToMono(GoogleUserInfo.class)
                .block();
        log.info("Google User INFO => {}", userInfo);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", userInfo.sub());
        attributes.put("username", userInfo.name());
        attributes.put("email", userInfo.email());
        attributes.put("first_name", userInfo.given_name());
        attributes.put("last_name", userInfo.family_name());
        attributes.put("picture", userInfo.picture());
        attributes.put("verified", userInfo.email_verified());
        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.name(), attributes, Collections.emptyList());
    }
}
