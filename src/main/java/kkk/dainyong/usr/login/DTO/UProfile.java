package kkk.dainyong.usr.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UProfile {
    Long id;
    String nickname;
    char gender;
    String birth;
    String image;
    String user_id;
}
