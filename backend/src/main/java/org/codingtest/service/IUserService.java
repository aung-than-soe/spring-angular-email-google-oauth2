package org.codingtest.service;

import org.codingtest.domain.User;
import org.codingtest.dto.GoogleUserInfo;
import org.codingtest.dto.UserDTO;

import java.util.Optional;

public interface IUserService {

    User saveGoogleUserInfo(GoogleUserInfo userInfo);

    Optional<User> findByEmail(String email);

}
