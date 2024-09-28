package kkk.dainyong.usr.login.controller;


import kkk.dainyong.usr.login.DTO.JWTResponse;
import kkk.dainyong.usr.login.DTO.ProfileSelectionRequest;
import kkk.dainyong.usr.login.DTO.UProfile;
import kkk.dainyong.usr.login.DTO.Users;
import kkk.dainyong.usr.login.jwt.JWTRefactor;
import kkk.dainyong.usr.login.service.CustomOAuth2User;
import kkk.dainyong.usr.login.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Log4j2

public class LoginController {
    private final ProfileService profileService;
    private final JWTRefactor jwtRefactor;

    @GetMapping("/my")
    public String getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            CustomOAuth2User loginUser = (CustomOAuth2User) oAuth2User;
            return "Logged in as: " + loginUser.getId() + loginUser.getProfileId();
        } else {
            return "NO login";
        }
    }

    @CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
    @GetMapping("/api/checkProfiles/{userId}")
    public ResponseEntity<List<UProfile>> checkProfiles(@PathVariable String userId) {
        log.info(userId);
        List<UProfile> Profiles = profileService.hasProfiles(userId);
        return ResponseEntity.ok(Profiles);
    }

    @CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
    @PostMapping("api/selectProfile")
    public ResponseEntity<?> selectProfile(@RequestBody ProfileSelectionRequest request, @RequestHeader("Authorization") String token) {
        // JWT 토큰에서 사용자 정보 추출
        String userId = jwtRefactor.getUserIdFromToken(token);
        log.info(userId);
        // 선택된 프로필 검증 및 처리
        UProfile selectedProfile = profileService.selectProfile(request.getProfileId());

        // 새로운 JWT 토큰 생성 (프로필 정보 포함)
        String newToken = jwtRefactor.createToken(userId, selectedProfile.getId(), "ROLE_USER");

        // 새 JWT 토큰을 응답으로 반환
        return ResponseEntity.ok(new JWTResponse(newToken));
    }



    @GetMapping("api/me")
    public ResponseEntity<Users> loginProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            CustomOAuth2User userTokenInfo = (CustomOAuth2User) oAuth2User;
            Users loginUser = profileService.loginUser(userTokenInfo.getId());
            log.info(loginUser);
            return ResponseEntity.ok(loginUser);
        }else{
            return null;
        }

        // JWT 토큰에서 사용자 정보 추출

    }
}
