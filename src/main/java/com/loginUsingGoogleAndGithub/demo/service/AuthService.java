package com.loginUsingGoogleAndGithub.demo.service;


import com.loginUsingGoogleAndGithub.demo.entity.User;
import com.loginUsingGoogleAndGithub.demo.repository.UserRepository;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> login(OAuth2AuthenticationToken authentication) {
        OAuth2User oauthUser = authentication.getPrincipal();

        // Extract user details
        String email = oauthUser.getAttribute("email");
        String provider = authentication.getAuthorizedClientRegistrationId();
        String providerId = oauthUser.getAttribute("id");

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setProvider(provider);
            user.setProviderId(providerId);
            userRepository.save(user);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        return response;
    }

    public Map<String, String> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return response;
    }
}
