package kkk.dainyong.usr.login.controller;


import kkk.dainyong.usr.login.service.CustomOAuth2User;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class LoginController {

    @GetMapping("/my")
    public String getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            CustomOAuth2User loginUser = (CustomOAuth2User) oAuth2User;
            return "Logged in as: " + loginUser.getId();
        } else {
            return "NO login";
        }
    }
}
