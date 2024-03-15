package com.example.usercredentials.controllers;

import com.example.usercredentials.models.UserModel;
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
    public ResponseEntity<List<UserModel>> listAllUsers() {
        List<UserModel> userModelList = userService.getAllUsers();
        return new ResponseEntity<>(userModelList, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserModel> findUserById(@PathVariable String id) {
        UserModel userModel = userService.findUserById(id);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @GetMapping("/ssn/{ssn}")
    public ResponseEntity<UserModel> findUserBySSN(@PathVariable String ssn) {
        UserModel userModel = userService.findUserBySsn(ssn);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addUser(@RequestBody UserModel userModel) {
        userService.addUser(userModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody UserModel userModel) {
        userService.updateUser(id, userModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
