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

    @GetMapping("/{role}/list")
    public ResponseEntity<List<UserModel>> listAllUsers(@PathVariable() String role) {
        List<UserModel> userList;
        if ("employee".equals(role)) {
            userList = userService.getAllEmployees();
        } else if ("visitor".equals(role)) {
            userList = userService.getAllVisitors();
        } else {
            return new ResponseEntity<>(List.of(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{role}/find/id/{id}")
    public ResponseEntity<UserModel> findUserById(@PathVariable String role, @PathVariable String id) {
        UserModel user;
        if ("employee".equals(role)) {
            user = userService.findEmployeeById(id);
        } else if ("visitor".equals(role)) {
            user = userService.findVisitorById(id);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{role}/find/ssn/{ssn}")
    public ResponseEntity<UserModel> findUserBySSN(@PathVariable String role, @PathVariable String ssn) {
        UserModel user;
        if ("employee".equals(role)) {
            user = userService.findEmployeeBySsn(ssn);
        } else if ("visitors".equals(role)) {
            user = userService.findVisitorBySsn(ssn);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/{role}/add")
    public ResponseEntity<Void> addUser(@PathVariable String role, @RequestBody UserModel userModel) {
        if ("employee".equals(role)) {
            userService.addEmployee(userModel);
        } else if ("visitors".equals(role)) {
            userService.addVisitor(userModel);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{role}/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String role, @PathVariable String id) {
        if ("employee".equals(role)) {
            userService.deleteEmployee(id);
        } else if ("visitors".equals(role)) {
            userService.deleteVisitor(id);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{role}/update/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String role, @PathVariable String id, @RequestBody UserModel userModel) {
        if ("employee".equals(role)) {
            userService.updateEmployee(id, userModel);
        } else if ("visitors".equals(role)) {
            userService.updateVisitor(id, userModel);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
