package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.Location;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService{
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private EmailSenderService mailSender;

    public UserWithoutPassDTO register(RegisterDTO dto) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password mismatch!");
        }
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exist!");
        }
        Location location = locationRepository.findByCountry(dto.getLocation()).orElseThrow(() -> new BadRequestException(NO_SUCH_COUNTRY));
        User user = mapper.map(dto, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setDateCreated(LocalDateTime.now());
        user.setLocation(location);
        user.setIsVerified(-1);
        user.setConfirmationToken(generateConfirmationToken());
        userRepository.save(user);
        sendConfirmationEmail(user);
        return mapper.map(user, UserWithoutPassDTO.class);
    }
    private String generateConfirmationToken(){
        return UUID.randomUUID().toString();
    }
    private void sendConfirmationEmail(User user){
        SimpleMailMessage message =new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirm your email");
        message.setText("Welcome and cheers, you just got registered.\n\n" +
                " To confirm your email, please click the link below:\n\n" +
                "http://localhost:8995/confirm?token=" + user.getConfirmationToken());
        new Thread(()->  mailSender.sendEmail(user.getEmail(),message.getSubject(), message.getText())).start();
    }
    public boolean confirmEmail(String token){
        User user = userRepository.findAllByConfirmationToken(token).orElseThrow(()-> new NotFoundException("No such token found"));
        user.setConfirmationToken(null);
        user.setIsVerified(1);
        userRepository.save(user);
        return true;
    }
    public UserWithoutPassDTO login(LoginDTO dto) {
        User user = userRepository.getUserByEmail(dto.getEmail()).orElseThrow(() -> new UnauthorizedException(WRONG_CREDENTIALS));
        if(!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(WRONG_CREDENTIALS);
        }
        dto.setIsVerified(user.getIsVerified());
        if(dto.getIsVerified() != 1) {
            throw new UnauthorizedException("You have to verify first!");
        }
        return mapper.map(user, UserWithoutPassDTO.class);
    }
    public UserWithoutPassDTO getById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(NO_SUCH_USER));
        return mapper.map(user, UserWithoutPassDTO.class);
    }
    public void deleteAccount(int loggedId) {
        userRepository.deleteById(loggedId);
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
        User user = userRepository.findById(loggedId).orElseThrow(() -> new BadRequestException(NO_SUCH_USER));
        Location location = locationRepository.findByCountry(dto.getLocation()).orElseThrow(() -> new BadRequestException(NO_SUCH_COUNTRY));
        User u = user;
        u.setLocation(location);
        u.setPassword(encoder.encode(dto.getPassword()));
        u.setEmail(dto.getEmail());
        u.setGender(dto.getGender());
        u.setDateCreated(u.getDateCreated());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setDateOfBirth(dto.getDateOfBirth());
        u.setProfilePicture(dto.getProfilePicture());
        u.setTelephone(dto.getTelephone());
        userRepository.save(u);
        return mapper.map(u, UserWithoutPassDTO.class);
    }

    public void verify(int loggedId) {
        User user = userRepository.findById(loggedId).orElseThrow(() -> new NotFoundException(NO_SUCH_USER));
        user.setIsVerified(1);
        userRepository.save(user);
    }
}