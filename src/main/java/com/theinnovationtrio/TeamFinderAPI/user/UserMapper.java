package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth.UserRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDto(List<User> users);

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "name", target = "userName")
    User mapToUser(AdminRegisterRequest adminRegisterRequest);

    default User mapToUser(AdminRegisterRequest adminRegisterRequest, String password) {
        User user = mapToUser(adminRegisterRequest);
        user.setPassword(password);
        return user;
    }

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "name", target = "userName")
    User mapToUser(UserRegisterRequest userRegisterRequest);

    default User mapToUser(UserRegisterRequest userRegisterRequest, String password) {
        User user = mapToUser(userRegisterRequest);
        user.setPassword(password);
        return user;
    }


}
