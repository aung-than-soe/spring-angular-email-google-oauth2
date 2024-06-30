package org.codingtest.dto;

public record UserDTO(
        String firstName,
        String lastName,
        String username,
        String email,
        String password) {
}
