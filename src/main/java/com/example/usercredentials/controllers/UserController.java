package com.example.usercredentials.controllers;

import com.example.usercredentials.models.UserDTO;
import com.example.usercredentials.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-credentials")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> listAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String id) {
        UserDTO userDTO = userService.findUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/ssn/{ssn}")
    public ResponseEntity<UserDTO> findUserBySSN(@PathVariable String ssn) {
        UserDTO userDTO = userService.findUserBySsn(ssn);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addUser(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
