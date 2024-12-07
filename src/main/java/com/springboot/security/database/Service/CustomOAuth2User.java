package com.springboot.security.database.Service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.Map;



public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;
    private String username;

    public CustomOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
        this.username = extractUsername(oAuth2User);
    }

    private String extractUsername(OAuth2User oAuth2User) {
        String name = oAuth2User.getAttribute("name");
        if (name != null && !name.isEmpty()) {
            return name;
        }

        String login = oAuth2User.getAttribute("login");
        if (login != null && !login.isEmpty()) {
            return login;
        }

       return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return username;
    }
}
