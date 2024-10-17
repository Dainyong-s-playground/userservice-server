package kkk.dainyong.usr.login.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kkk.dainyong.usr.login.DTO.CreateProfile;
import kkk.dainyong.usr.login.DTO.JWTResponse;
import kkk.dainyong.usr.login.DTO.ProfileSelectionRequest;
import kkk.dainyong.usr.login.DTO.UProfile;
import kkk.dainyong.usr.login.jwt.JWTRefactor;
import kkk.dainyong.usr.login.service.CustomOAuth2User;
import kkk.dainyong.usr.login.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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


    @CrossOrigin(origins = "https://www.dainyongplayground.site", allowCredentials = "true")
    @GetMapping("/api/checkProfiles/{userId}")
    public ResponseEntity<List<UProfile>> checkProfiles(@PathVariable String userId) {
        log.info(userId);
        List<UProfile> Profiles = profileService.hasProfiles(userId);
        return ResponseEntity.ok(Profiles);
    }

    @CrossOrigin(origins = "https://www.dainyongplayground.site", allowCredentials = "true")
    @PostMapping("api/selectProfile")
    public ResponseEntity<?> selectProfile(@RequestBody ProfileSelectionRequest request, @RequestHeader("Authorization") String token, HttpServletResponse response) {
        // JWT 토큰에서 사용자 정보 추출
        String userId = jwtRefactor.getUserIdFromToken(token);
        log.info(userId);

        // 선택된 프로필 검증 및 처리
        UProfile selectedProfile = profileService.selectProfile(request.getProfileId());

        // 새로운 JWT 토큰 생성 (프로필 정보 포함)
        String newToken = jwtRefactor.createToken(userId, selectedProfile.getId(), "ROLE_USER");

        // 쿠키 업데이트
        Cookie jwtCookie = new Cookie("Authorization", newToken);


        jwtCookie.setPath("/"); // 쿠키가 모든 경로에 대해 유효하도록 설정
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 1주일
        jwtCookie.setSecure(true);
        jwtCookie.setDomain("dainyongplayground.site");// 쿠키의 유효 기간을 1시간으로 설정
        response.addCookie(jwtCookie); // 응답에 쿠키 추가

        // 새 JWT 토큰을 응답으로 반환
        return ResponseEntity.ok(new JWTResponse(newToken));
    }


    @GetMapping("api/me")
    public ResponseEntity<UProfile> loginProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            CustomOAuth2User userTokenInfo = (CustomOAuth2User) oAuth2User;
            UProfile loginUser = profileService.selectProfile(userTokenInfo.getProfileId());
            log.info(loginUser);
            return ResponseEntity.ok(loginUser);
        } else {
            return null;
        }
    }

    @PostMapping("/api/createProfile")
    public ResponseEntity<?> createProfile(@RequestBody @Valid CreateProfile profileRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            CustomOAuth2User userTokenInfo = (CustomOAuth2User) oAuth2User;
            // JWT 토큰 검증 및 유저 ID 추출
            String userId = userTokenInfo.getId();

            // 서비스에 전달하여 프로필 생성 처리
            profileService.createProfile(userId, profileRequest);

            // 성공적으로 생성된 경우 200 OK 응답
            return ResponseEntity.ok("프로필이 성공적으로 생성되었습니다.");
        } else {
            // 실패한 경우 500 Internal Server Error 응답
            return ResponseEntity.status(500).body("프로필 생성 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/api/editProfile/{profileId}")
    public ResponseEntity<?> UpdateProfile(@RequestBody @Valid CreateProfile profileRequest, @PathVariable("profileId") Long profileId) {

        try{
            // 서비스에 전달하여 프로필 생성 처리
            profileService.updateProfile(profileId, profileRequest);

            // 성공적으로 생성된 경우 200 OK 응답
            return ResponseEntity.ok("프로필이 성공적으로 수정되었습니다.");
        } catch(Exception e) {
            // 실패한 경우 500 Internal Server Error 응답
            return ResponseEntity.status(500).body(e.toString());
        }
    }

    @DeleteMapping("/api/deleteProfile/{profileId}")
    public ResponseEntity<String> deleteProfile(@PathVariable("profileId") Long profileId) {
        log.info(profileId);
        profileService.deleteProfile(profileId);
        return ResponseEntity.ok("프로필이 성공적으로 삭제되었습니다.");
    }
}
