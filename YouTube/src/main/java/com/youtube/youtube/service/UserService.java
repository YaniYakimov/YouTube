package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.LoginDTO;
import com.youtube.youtube.model.DTOs.RegisterDTO;
import com.youtube.youtube.model.DTOs.UserBasicInfoDTO;
import com.youtube.youtube.model.DTOs.UserWithoutPassDTO;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends AbstractService{
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JavaMailSender mailSender;
    public UserWithoutPassDTO register(RegisterDTO dto) {
//        if(!dto.password().equals(dto.confirmPassword())) {
//            throw new BadRequestException("Password mismatch!");
//        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already exist!");
        }
        UserMapper userMapper1 = UserMapper.INSTANCE;
        User user = userMapper1.dtoToUser(dto);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        mailSender.sendEmail(user.getEmail(), "Welcome", "Cheers, you just got registered");
        UserMapper userMapper2 = UserMapper.INSTANCE;
        return userMapper2.user(user);
    }

    public UserWithoutPassDTO login(LoginDTO dto) {
        Optional<User> user = userRepository.getUserByEmail(dto.email());
        if(!user.isPresent()) {
            throw new UnauthorizedException("Wrong credentials");
        }
        if(!encoder.matches(dto.password(), user.get().getPassword())) {
            throw new UnauthorizedException("Wrong credentials");
        }
        UserMapper userMapper = UserMapper.INSTANCE;
        UserWithoutPassDTO userDto = userMapper.user(user.get());
        return userDto;
    }

    public UserWithoutPassDTO getById(int id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw  new NotFoundException("There is no such user");
        }
        UserMapper userMapper = UserMapper.INSTANCE;
        return userMapper.user(user.get());
    }

    public void deleteAccount(int loggedId) {
        userRepository.deleteById(loggedId);
    }

    public void logOut(HttpSession session) {
        session.invalidate();
    }

    public List<User> getUserByName(UserBasicInfoDTO dto) {
        List<User> result = userRepository.getUserByFirstName(dto.firstName());
        if(result.isEmpty()) {
            throw new NotFoundException("There is/are no user/users with this name!");
        }
        return result;
    }

    public int subscribe(int subscriberId, int subscribedId) {
        User subscriber = getUserById(subscriberId);
        User subscribed = getUserById(subscribedId);
        if(subscribed.getSubscribers().contains(subscriber)) {
            subscribed.getSubscribers().remove(subscriber);
        }
        else {
            subscribed.getSubscribers().add(subscriber);
        }
        return subscribed.getSubscribers().size();
    }

}
