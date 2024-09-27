package kkk.dainyong.usr.login.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JWTRefactor {

    private final JWTUtil jwtUtil;


    public String createToken(String userId, Long profileId, String role) {
        // You can include other claims like nickname, gender, birth if necessary
        return jwtUtil.refactorToken(userId, profileId, role);
    }

    public String getUserIdFromToken(String token) {
        return jwtUtil.getId(token);  // Delegate to JWTUtil for claim extraction
    }

    public Boolean isTokenExpired(String token) {
        return jwtUtil.isExpired(token);  // Reuse method from JWTUtil
    }

    public Long getProfileNameFromToken(String token) {
        return jwtUtil.getProfileId(token);  // You might need to add this method to JWTUtil
    }
}
