package kkk.dainyong.usr.login.DTO;

import java.util.HashMap;
import java.util.Map;

public class NaverResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.getOrDefault("response", new HashMap<>());
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attribute;
    }

    @Override
    public String getGender() {
        return attribute.get("gender").toString();
    }

    @Override
    public String getBirthday() {
        return attribute.get("birthday").toString();
    }
}
