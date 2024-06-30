package org.codingtest.dto;

public record GoogleUserInfo(String sub, String name, String given_name, String family_name, String picture,
                             String email, boolean email_verified, String locale) {
}
