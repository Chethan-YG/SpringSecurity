 package com.springboot.security.database.config;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.springboot.security.database.model.User;
import com.springboot.security.database.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private UserRepository userRepository;

    @Bean
    private BCryptPasswordEncoder passwordEncoder1()
    {
    	return new BCryptPasswordEncoder();
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        User user1 = new User();

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            handleGoogleLogin(user, user1, request);
        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            handleGithubLogin(user, user1, request);
        }

        
        new DefaultRedirectStrategy().sendRedirect(request, response, "/welcome");
    }

    private void handleGoogleLogin(DefaultOAuth2User user, User user1, HttpServletRequest request) {
        String name = user.getAttribute("name").toString();
        String email = user.getAttribute("email").toString();
        String profilePicture = user.getAttribute("picture").toString();    
        saveUserIfNotExists(email, name, profilePicture, user1);
       
    }

    private void handleGithubLogin(DefaultOAuth2User user, User user1, HttpServletRequest request) {
        String name = user.getAttribute("login").toString();
        String email = user.getAttribute("email");
        if (email == null || email.isEmpty()) {
            email = name + "@gmail.com";
        }
        String profilePicture = user.getAttribute("avatar_url").toString();
        saveUserIfNotExists(email, name, profilePicture, user1);
        
    }

    

    private void saveUserIfNotExists(String email, String name,String profilePicture,User user1) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (!existingUser.isPresent()) {
            user1.setEmail(email);
            user1.setUsername(name);
            user1.setPassword(passwordEncoder1().encode("Ht7#Kl2$"));
            user1.setPicture(profilePicture);
            userRepository.save(user1);
        }
    }
}