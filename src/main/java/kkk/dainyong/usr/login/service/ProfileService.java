package kkk.dainyong.usr.login.service;

import kkk.dainyong.usr.login.DTO.UProfile;
import kkk.dainyong.usr.login.DTO.Users;
import kkk.dainyong.usr.login.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final IUserRepository userRepository;

    public List<UProfile> hasProfiles(String email){
        List<UProfile> list_UProfile = userRepository.findByEmailForProfile(email);
        if(list_UProfile != null) {
            return list_UProfile;
        }else{
            return null;
        }
    }

    public UProfile selectProfile(Long profileId) {
        // 프로필이 해당 사용자에 속하는지 확인
        UProfile profile = userRepository.findByProfileId(profileId);
        if(profile==null){
            return null;
        }
        return profile;
    }

    public Users loginUser(String id){
        Users loginUser = userRepository.findByEmail(id);
        if(loginUser==null){
            return null;
        }
        return loginUser;
    }
}
