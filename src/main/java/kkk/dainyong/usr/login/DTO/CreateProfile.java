package kkk.dainyong.usr.login.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProfile {
    @NotNull
    private String nickname;
    @NotNull
    private char gender;
    @NotNull
    private String birth;
    @NotNull
    private String image;

}
