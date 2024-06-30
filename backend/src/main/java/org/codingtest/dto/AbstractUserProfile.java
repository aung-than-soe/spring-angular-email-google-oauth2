package org.codingtest.dto;

import lombok.Builder;

@Builder
public record AbstractUserProfile(String username, String email, String first_name, String last_name, String picture,
                                  String authType) {
}
