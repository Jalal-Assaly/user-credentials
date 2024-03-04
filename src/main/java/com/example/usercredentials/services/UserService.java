package com.example.usercredentials.services;

import com.example.usercredentials.models.UserDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO findUserById(String id);
    UserDTO findUserBySsn(String SSN);
    void addUser(@Valid UserDTO userDTO);
    void deleteUser(String id);
    void updateUser(String id, @Valid UserDTO userDTO);
}
