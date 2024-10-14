package kkk.dainyong.usr.login.repository;

import kkk.dainyong.usr.login.DTO.UProfile;
import kkk.dainyong.usr.login.DTO.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IUserRepository {
    Users findByEmail(String email);
    void insert(Users entity);
    void updateProfile(UProfile entity);
    void delete(String email);
    List<UProfile> findByEmailForProfile(String email);
    UProfile findByProfileId(Long profileId);
    void insertProfiles(UProfile entity);
}
