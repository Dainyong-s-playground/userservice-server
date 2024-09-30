package kkk.dainyong.usr.login.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserToken {
    String id;
    Long profileId;
}
