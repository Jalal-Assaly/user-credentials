package com.example.usercredentials.services;

import com.example.usercredentials.documents.User;
import com.example.usercredentials.models.UserModel;
import com.example.usercredentials.models.mappers.UserMapper;
import com.example.usercredentials.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserModel> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toUserModel)
                .toList();
    }

    public UserModel findUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));
        return userMapper.toUserModel(user);
    }

    public UserModel findUserBySsn(String ssn) {
        User user = userRepository.findUserBySsn(ssn)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));
        return userMapper.toUserModel(user);
    }

    public void addUser(@Valid UserModel userModel) {
        User user = userMapper.toUser(userModel);
        if(userRepository.existsById(user.getId())) {
            throw new EntityExistsException("User already exists");
        } else {
            userRepository.save(user);
        }
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));
        userRepository.delete(user);
    }

    public void updateUser(String id, @Valid UserModel userModel) {
        if(!id.equals(userModel.getId())) throw new ValidationException("Path ID and Request ID not matching");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));
        BeanUtils.copyProperties(userModel, user, "id");
        userRepository.save(user);
    }
}
