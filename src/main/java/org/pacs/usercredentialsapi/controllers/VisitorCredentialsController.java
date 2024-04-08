package org.pacs.usercredentialsapi.controllers;

import lombok.RequiredArgsConstructor;
import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.services.VisitorCredentialsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-credentials/visitor")
public class VisitorCredentialsController {

    private final VisitorCredentialsService visitorCredentialsService;

    @GetMapping("/list")
    public ResponseEntity<List<UserCredentialsModel>> listAllVisitors() {
        List<UserCredentialsModel> userList = visitorCredentialsService.getAllVisitors();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/find/id/{id}")
    public ResponseEntity<UserCredentialsModel> findUserById(@PathVariable String id) {
        UserCredentialsModel user = visitorCredentialsService.findVisitorById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/find/email/{email}")
    public ResponseEntity<UserCredentialsModel> findVisitorByEmail(@PathVariable String email) {
        UserCredentialsModel user = visitorCredentialsService.findVisitorByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addVisitor(@RequestBody UserCredentialsModel userCredentialsModel) {
        visitorCredentialsService.addNewVisitor(userCredentialsModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable String id) {
        visitorCredentialsService.deleteVisitor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateVisitor(@PathVariable String id, @RequestBody UserCredentialsModel userCredentialsModel) {
        visitorCredentialsService.updateVisitor(id, userCredentialsModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
