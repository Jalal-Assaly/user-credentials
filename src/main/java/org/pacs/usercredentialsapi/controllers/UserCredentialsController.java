package org.pacs.usercredentialsapi.controllers;

import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.services.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-credentials")
public class UserCredentialsController {

    private final UserCredentialsService userCredentialsService;

    @GetMapping("/employee/list")
    public ResponseEntity<List<UserCredentialsModel>> listAllEmployees() {
        List<UserCredentialsModel> userList = userCredentialsService.getAllEmployees();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/visitor/list")
    public ResponseEntity<List<UserCredentialsModel>> listAllVisitors() {
        List<UserCredentialsModel> userList = userCredentialsService.getAllVisitors();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/employee/find/id/{id}")
    public ResponseEntity<UserCredentialsModel> findEmployeeById(@PathVariable String id) {
        UserCredentialsModel user = userCredentialsService.findEmployeeById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/visitor/find/id/{id}")
    public ResponseEntity<UserCredentialsModel> findUserById(@PathVariable String id) {
        UserCredentialsModel user = userCredentialsService.findVisitorById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/employee/find/email/{email}")
    public ResponseEntity<UserCredentialsModel> findEmployeeByEmail(@PathVariable String email) {
        UserCredentialsModel user = userCredentialsService.findEmployeeByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/visitor/find/email/{email}")
    public ResponseEntity<UserCredentialsModel> findVisitorByEmail(@PathVariable String email) {
        UserCredentialsModel user = userCredentialsService.findVisitorByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/employee/add")
    public ResponseEntity<Void> addEmployee(@RequestBody UserCredentialsModel userCredentialsModel) {
        userCredentialsService.addNewEmployee(userCredentialsModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/visitor/add")
    public ResponseEntity<Void> addVisitor(@RequestBody UserCredentialsModel userCredentialsModel) {
        userCredentialsService.addNewVisitor(userCredentialsModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/employee/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        userCredentialsService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/visitor/delete/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable String id) {
        userCredentialsService.deleteVisitor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/employee/update/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String id, @RequestBody UserCredentialsModel userCredentialsModel) {
        userCredentialsService.updateEmployee(id, userCredentialsModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/visitor/update/{id}")
    public ResponseEntity<Void> updateVisitor(@PathVariable String id, @RequestBody UserCredentialsModel userCredentialsModel) {
        userCredentialsService.updateVisitor(id, userCredentialsModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
