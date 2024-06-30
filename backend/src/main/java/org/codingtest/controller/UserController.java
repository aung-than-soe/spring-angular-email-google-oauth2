package org.codingtest.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.codingtest.dto.AbstractUserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("api")
public class UserController {

    @GetMapping(value = "profile")
    public ResponseEntity<AbstractUserProfile> getUserProfile(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal, HttpServletRequest request) {
        Map<String, Object> attributes = principal.getAttributes();
        AbstractUserProfile userProfile = AbstractUserProfile.builder()
                .username((String) attributes.get("username"))
                .email(attributes.get("email").toString())
                .first_name(attributes.get("first_name").toString())
                .last_name(attributes.get("last_name").toString())
                .picture(attributes.containsKey("picture")?attributes.get("picture").toString():"")
                .authType(request.getHeader("auth"))
                .build();
        return ResponseEntity.ok(userProfile);
    }
}
