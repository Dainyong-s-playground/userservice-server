package kkk.dainyong.usr.login.service;


import kkk.dainyong.usr.login.DTO.NaverResponse;
import kkk.dainyong.usr.login.DTO.OAuth2Response;
import kkk.dainyong.usr.login.DTO.UserToken;
import kkk.dainyong.usr.login.DTO.Users;
import kkk.dainyong.usr.login.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final IUserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if ("naver".equals(registrationId)) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else {
            throw new OAuth2AuthenticationException("Unsupported OAuth2 provider");
        }

        Users existingMember = userRepository.findByEmail(oAuth2Response.getEmail());


        String role = "ROLE_USER";
        if (existingMember == null) {
            Users newMember = Users.builder()
                    .id(oAuth2Response.getEmail())
                    .nickname(oAuth2Response.getName())
                    .gender(oAuth2Response.getGender())
                    .birth(oAuth2Response.getBirthday())
                    .build();
            userRepository.insert(newMember);
            UserToken token = UserToken.builder().id(newMember.getId()).profileId(null).build();
            return new CustomOAuth2User(token, role);
        } else {
            Users userDTO = Users.builder()
                    .id(oAuth2Response.getEmail())
                    .nickname(oAuth2Response.getName())
                    .gender(oAuth2Response.getGender())
                    .birth(oAuth2Response.getBirthday())
                    .build();
            UserToken token = UserToken.builder().id(userDTO.getId()).profileId(null).build();
            return new CustomOAuth2User(token, role);
        }
    }
}