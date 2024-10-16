package kkk.dainyong.usr.login.service;

import kkk.dainyong.usr.login.DTO.CreateProfile;
import kkk.dainyong.usr.login.DTO.UProfile;
import kkk.dainyong.usr.login.DTO.Users;
import kkk.dainyong.usr.login.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Log4j2
public class ProfileService {
    private final IUserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UProfile> hasProfiles(String email) {
        List<UProfile> list_UProfile = userRepository.findByEmailForProfile(email);
        if (list_UProfile != null) {
            return list_UProfile;
        } else {
            return null;
        }
    }
    @Transactional(readOnly = true)
    public UProfile selectProfile(Long profileId) {
        // 프로필이 해당 사용자에 속하는지 확인
        UProfile profile = userRepository.findByProfileId(profileId);
        if (profile == null) {
            return null;
        }
        return profile;
    }

    @Transactional(readOnly = true)
    public Users loginUser(String id) {
        Users loginUser = userRepository.findByEmail(id);
        if (loginUser == null) {
            return null;
        }
        return loginUser;
    }

    public void createProfile(String userId, CreateProfile newProfile) {
        UProfile profile = UProfile.builder()
                .nickname(newProfile.getNickname())
                .gender(newProfile.getGender())
                .birth(newProfile.getBirth())
                .image(newProfile.getImage())
                .user_id(userId)
                .build();

        userRepository.insertProfiles(profile);
        Long savedId = profile.getId();
        userRepository.insertReports(savedId);
    }

    public void updateProfile(Long profileId, CreateProfile newProfile) {
        UProfile profile = UProfile.builder()
                .id(profileId)
                .nickname(newProfile.getNickname())
                .gender(newProfile.getGender())
                .birth(newProfile.getBirth())
                .image(newProfile.getImage())
                .build();

        userRepository.updateProfile(profile);
    }

    public void deleteProfile(Long profileId){
        log.info(profileId);
        userRepository.deleteProfile(profileId);
    }
}
