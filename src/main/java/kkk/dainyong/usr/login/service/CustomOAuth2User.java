package kkk.dainyong.usr.login.service;

import kkk.dainyong.usr.login.DTO.OAuth2Response;
import kkk.dainyong.usr.login.DTO.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Users users;
    private final String role;


    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });
        log.info("collection: {}", collection);
        return collection;
    }

    @Override
    public String getName() {
        return users.getNickname();
    }


    public String getGender(){
        return users.getGender();
    }

    public String getBirthday(){
        return users.getBirth();
    }

    public String getId(){
        return users.getId();
    }
}

