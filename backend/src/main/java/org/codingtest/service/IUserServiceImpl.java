package org.codingtest.service;

import lombok.RequiredArgsConstructor;
import org.codingtest.domain.User;
import org.codingtest.dto.GoogleUserInfo;
import org.codingtest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User saveGoogleUserInfo(GoogleUserInfo userInfo) {
        User user = User.builder()
                .email(userInfo.email())
                .firstName(userInfo.given_name())
                .lastName(userInfo.family_name())
                .username(userInfo.name())
                .token("")
                .build();
        this.userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

}
