package kkk.dainyong.usr.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    String id;
    String nickname;
    String gender;
    String birth;
}
