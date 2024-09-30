package kkk.dainyong.usr.login.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JWTResponse {
    private String token;

    public JWTResponse(String token) {
        this.token = token;
    }
}
