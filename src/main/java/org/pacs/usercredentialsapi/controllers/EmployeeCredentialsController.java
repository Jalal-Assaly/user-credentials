package org.pacs.usercredentialsapi.controllers;

import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.services.EmployeeCredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-credentials/employee")
public class EmployeeCredentialsController {

    private final EmployeeCredentialsService employeeCredentialsService;

    @GetMapping("/list")
    public ResponseEntity<List<UserCredentialsModel>> listAllEmployees() {
        List<UserCredentialsModel> userList = employeeCredentialsService.getAllEmployees();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/employee/find/id/{id}")
    public ResponseEntity<UserCredentialsModel> findEmployeeById(@PathVariable String id) {
        UserCredentialsModel user = employeeCredentialsService.findEmployeeById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/employee/find/email/{email}")
    public ResponseEntity<UserCredentialsModel> findEmployeeByEmail(@PathVariable String email) {
        UserCredentialsModel user = employeeCredentialsService.findEmployeeByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/employee/add")
    public ResponseEntity<Void> addEmployee(@RequestBody UserCredentialsModel userCredentialsModel) {
        employeeCredentialsService.addNewEmployee(userCredentialsModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/employee/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        employeeCredentialsService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/employee/update/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String id, @RequestBody UserCredentialsModel userCredentialsModel) {
        employeeCredentialsService.updateEmployee(id, userCredentialsModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
