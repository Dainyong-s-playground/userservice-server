package kkk.dainyong.usr.login.repository;

import kkk.dainyong.usr.login.DTO.Users;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface IUserRepository {
    Users findByEmail(String email);
    void insert(Users entity);
    void update(Users entity);
    void delete(String email);
}
