package kkk.dainyong.usr.login.DTO;

import java.util.Map;

public interface OAuth2Response {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getGender();

    String getBirthday();

    Map<String, Object> getAttributes();
}
