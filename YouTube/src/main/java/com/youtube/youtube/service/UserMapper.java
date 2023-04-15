package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.UserWithoutPassDTO;
import com.youtube.youtube.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserWithoutPassDTO user(User user);
}
