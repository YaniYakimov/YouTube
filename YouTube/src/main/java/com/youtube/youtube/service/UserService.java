package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.Location;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService{
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JavaMailSender mailSender;
    public UserWithoutPassDTO register(RegisterDTO dto) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password mismatch!");
        }
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exist!");
        }
        Optional<Location> location = locationRepository.findByCountry(dto.getLocation());
        User user = mapper.map(dto, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setDateCreated(LocalDateTime.now());
        if(location.isPresent()) {
            user.setLocation(location.get());
        }
        else {
            throw new BadRequestException("No such country!");
        }
        userRepository.save(user);
//        mailSender.sendEmail("qniqkimov@gmail.com", "Welcome", "Cheers, you just got registered");
        return mapper.map(user, UserWithoutPassDTO.class);
    }

    public UserWithoutPassDTO login(LoginDTO dto) {
        Optional<User> user = userRepository.getUserByEmail(dto.getEmail());
        if(!user.isPresent()) {
            throw new UnauthorizedException("Wrong credentials");
        }
        if(!encoder.matches(dto.getPassword(), user.get().getPassword())) {
            throw new UnauthorizedException("Wrong credentials");
        }
        return mapper.map(user, UserWithoutPassDTO.class);
    }

    public UserWithoutPassDTO getById(int id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw  new NotFoundException("There is no such user");
        }
        return mapper.map(user, UserWithoutPassDTO.class);
    }

    public void deleteAccount(int loggedId) {
        userRepository.deleteById(loggedId);
    }

    public void logOut(HttpSession session) {
        session.invalidate();
    }

    public List<UserWithoutPassDTO> getUserByName(UserBasicInfoDTO dto) {
        List<UserWithoutPassDTO> result = userRepository.getUserByFirstName(dto.getFirstName())
                .stream()
                .map(u -> mapper.map(u, UserWithoutPassDTO.class))
                .collect(Collectors.toList());
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
        userRepository.save(subscribed);
        return subscribed.getSubscribers().size();
    }

    public UserWithoutPassDTO edit(RegisterDTO dto, int loggedId) {
        Optional<User> user = userRepository.findById(loggedId);
        if(!user.isPresent()) {
            throw new BadRequestException("No such user!");
        }
        Optional<Location> location = locationRepository.findByCountry(dto.getLocation());
        User u = user.get();
        if(location.isPresent()) {
            u.setLocation(location.get());
        }
        else {
            throw new BadRequestException("No such country!");
        }
        u.setPassword(dto.getPassword());
        u.setEmail(dto.getEmail());
        u.setGender(dto.getGender());
        u.setDateCreated(dto.getDateCreated());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setDateOfBirth(dto.getDateOfBirth());
        u.setProfilePicture(dto.getProfilePicture());
        u.setTelephone(dto.getTelephone());
        userRepository.save(u);
        return mapper.map(u, UserWithoutPassDTO.class);
    }
}

