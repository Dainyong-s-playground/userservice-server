package kkk.dainyong.usr.login.service;


import kkk.dainyong.usr.login.DTO.NaverResponse;
import kkk.dainyong.usr.login.DTO.OAuth2Response;
import kkk.dainyong.usr.login.DTO.Users;
import kkk.dainyong.usr.login.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            Users newMember = new Users();
            newMember.setId(oAuth2Response.getEmail());
            newMember.setNickname(oAuth2Response.getName());
            newMember.setGender(oAuth2Response.getGender());
            newMember.setBirth(oAuth2Response.getBirthday());// OAuth2 로그인의 경우 비밀번호가 필요 없을 수 있습니다.
            userRepository.insert(newMember);

            return new CustomOAuth2User(newMember, role);
        }else{
            Users userDTO = new Users();
            userDTO.setId(existingMember.getId());
            userDTO.setNickname(existingMember.getNickname());
            userDTO.setGender(existingMember.getGender());
            userDTO.setBirth(existingMember.getBirth());

            return new CustomOAuth2User(userDTO,role);
        }
    }
}