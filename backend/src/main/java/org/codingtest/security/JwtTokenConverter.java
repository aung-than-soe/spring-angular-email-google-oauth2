package org.codingtest.security;

import lombok.RequiredArgsConstructor;
import org.codingtest.service.IUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtTokenConverter implements OpaqueTokenAuthenticationConverter {

    private final IUserService userService;

//    @Override
//    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
//        String email = (String) jwt.getClaims().get("email");
//        User user = this.userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("user email {0} not found!", email)));
//        user.setPassword("");
//        return new UsernamePasswordAuthenticationToken(user, jwt, Collections.emptyList());
//    }

    @Override
    public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
//        String email = (String) jwt.getClaims().get("email");
//        User user = this.userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("user email {0} not found!", email)));
//        user.setPassword("");
        return new UsernamePasswordAuthenticationToken(authenticatedPrincipal, introspectedToken, Collections.emptyList());
    }
}
