package com.example.usercredentials.services;

import com.example.usercredentials.models.UserModel;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface UserService {
    List<UserModel> getAllUsers();
    UserModel findUserById(String id);
    UserModel findUserBySsn(String SSN);
    void addUser(@Valid UserModel userModel);
    void deleteUser(String id);
    void updateUser(String id, @Valid UserModel userModel);
}
