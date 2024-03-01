package com.example.usercredentials.services;

import com.example.usercredentials.models.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();
    UserDTO findUserById(String id);
    UserDTO findUserBySsn(String SSN);
    void addUser(UserDTO userDTO);
    void deleteUser(String id);
    void updateUser(String id, UserDTO userDTO);

}
