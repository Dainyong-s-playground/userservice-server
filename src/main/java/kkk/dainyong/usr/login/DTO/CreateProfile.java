package kkk.dainyong.usr.login.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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
