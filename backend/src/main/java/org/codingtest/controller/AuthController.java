package org.codingtest.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codingtest.domain.User;
import org.codingtest.dto.GoogleURI;
import org.codingtest.dto.LoginRequest;
import org.codingtest.dto.TokenDTO;
import org.codingtest.dto.UserDTO;
import org.codingtest.security.TokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class AuthController {

    @Value("${spring.security.oauth2.resource-server.opaque-token.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resource-server.opaque-token.client-secret}")
    private String clientSecret;

    private final TokenGenerator tokenGenerator;
    private final UserDetailsManager userDetailsManager;

    @GetMapping("auth/url")
    public ResponseEntity<GoogleURI> getURI() {
        String url = new GoogleAuthorizationCodeRequestUrl(clientId,
                "http://localhost:4200",
                Arrays.asList("email", "profile", "openid")).build();
        return ResponseEntity.ok(new GoogleURI(url));
    }

    @GetMapping("auth/callback")
    public ResponseEntity<TokenDTO> callback(@RequestParam("code") String code) {
        try {
            Map<String, Object> valueMap = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    clientId,
                    clientSecret,
                    code,
                    "http://localhost:4200"
            )
                    .execute();
            return ResponseEntity.ok(new TokenDTO((String) valueMap.get("access_token"), (String) valueMap.get("token_type"), (Long) valueMap.get("expires_in"), "google"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("createaccount")
    public ResponseEntity<TokenDTO> createAccount(@RequestBody() UserDTO userDto) {
        User user = User.mapToEntity(userDto);
        this.userDetailsManager.createUser(user);
        TokenDTO tokenInfo = authenticate(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenInfo);
    }

    @PostMapping("login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginRequest data) {
        User user = (User) this.userDetailsManager.loadUserByUsername(data.email());
        TokenDTO tokenInfo = authenticate(user);
        return ResponseEntity.ok(tokenInfo);
    }

    private TokenDTO authenticate(User user) {
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), Collections.emptyList());
        Map<String, Object> map = tokenGenerator.generateToken(authentication);
        Long expiredIn = (Long) Objects.requireNonNull(map.get("expires_in"));
        return new TokenDTO((String) map.get("token"), "Bearer", expiredIn, "default");
    }
}
