package org.codingtest.dto;


public record TokenDTO(String access_token, String token_type, Long expires_in, String auth_type) {
}
