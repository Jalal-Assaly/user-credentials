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

    @GetMapping("/employee/list")
    public ResponseEntity<List<UserModel>> listAllEmployees() {
        List<UserModel> userList = userService.getAllEmployees();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/visitor/list")
    public ResponseEntity<List<UserModel>> listAllVisitors() {
        List<UserModel> userList = userService.getAllVisitors();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/employee/find/id/{id}")
    public ResponseEntity<UserModel> findEmployeeById(@PathVariable String id) {
        UserModel user = userService.findEmployeeById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/visitor/find/id/{id}")
    public ResponseEntity<UserModel> findUserById(@PathVariable String id) {
        UserModel user = userService.findVisitorById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/employee/find/email")
    public ResponseEntity<UserModel> findEmployeeByEmail(@RequestParam String email) {
        UserModel user = userService.findEmployeeByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/visitor/find/email")
    public ResponseEntity<UserModel> findVisitorByEmail(@RequestParam String email) {
        UserModel user = userService.findVisitorByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/employee/add")
    public ResponseEntity<Void> addEmployee(@RequestBody UserModel userModel) {
        userService.addEmployee(userModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/visitor/add")
    public ResponseEntity<Void> addVisitor(@RequestBody UserModel userModel) {
        userService.addVisitor(userModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/employee/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        userService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/visitor/delete/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable String id) {
        userService.deleteVisitor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/employee/update/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String id, @RequestBody UserModel userModel) {
        userService.updateEmployee(id, userModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/visitor/update/{id}")
    public ResponseEntity<Void> updateVisitor(@PathVariable String id, @RequestBody UserModel userModel) {
        userService.updateVisitor(id, userModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
