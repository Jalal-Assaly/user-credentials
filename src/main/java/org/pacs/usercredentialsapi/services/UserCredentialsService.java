package org.pacs.usercredentialsapi.services;

import org.pacs.usercredentialsapi.documents.EmployeeCredentials;
import org.pacs.usercredentialsapi.documents.VisitorCredentials;
import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.models.mappers.UserMapper;
import org.pacs.usercredentialsapi.repositories.EmployeeCredentialsRepository;
import org.pacs.usercredentialsapi.repositories.VisitorCredentialsRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCredentialsService {

    private final EmployeeCredentialsRepository employeeCredentialsRepository;
    private final VisitorCredentialsRepository visitorRepository;
    private final UserMapper userMapper;

    public List<UserCredentialsModel> getAllEmployees() {
        return employeeCredentialsRepository.findAll()
                .stream()
                .map(userMapper::toUserModel)
                .toList();
    }

    public List<UserCredentialsModel> getAllVisitors() {
        return visitorRepository.findAll().stream()
                .map(userMapper::toUserModel)
                .toList();
    }

    public UserCredentialsModel findEmployeeById(String id) {
        EmployeeCredentials employeeCredentials = employeeCredentialsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        return userMapper.toUserModel(employeeCredentials);
    }

    public UserCredentialsModel findVisitorById(String id) {
        VisitorCredentials visitorCredentials = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        return userMapper.toUserModel(visitorCredentials);
    }

    public UserCredentialsModel findEmployeeByEmail(String email) {
        EmployeeCredentials employeeCredentials = employeeCredentialsRepository.findEmployeeByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        return userMapper.toUserModel(employeeCredentials);
    }

    public UserCredentialsModel findVisitorByEmail(String email) {
        VisitorCredentials visitorCredentials = visitorRepository.findVisitorByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        return userMapper.toUserModel(visitorCredentials);
    }

    public void addNewEmployee(@Valid UserCredentialsModel employeeModel) {
        EmployeeCredentials employeeCredentials = userMapper.toEmployee(employeeModel);
        if(employeeCredentialsRepository.existsById(employeeCredentials.getId())) {
            throw new EntityExistsException("Employee already exists");
        } else {
            employeeCredentialsRepository.save(employeeCredentials);
        }
    }

    public void addNewVisitor(@Valid UserCredentialsModel visitorModel) {
        VisitorCredentials visitorCredentials = userMapper.toVisitor(visitorModel);
        if(visitorRepository.existsById(visitorCredentials.getId())) {
            throw new EntityExistsException("Visitor already exists");
        } else {
            visitorRepository.save(visitorCredentials);
        }
    }

    public void deleteEmployee(String id) {
        EmployeeCredentials employeeCredentials = employeeCredentialsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        employeeCredentialsRepository.delete(employeeCredentials);
    }

    public void deleteVisitor(String id) {
        VisitorCredentials visitorCredentials = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        visitorRepository.delete(visitorCredentials);
    }

    public void updateEmployee(String id, @Valid UserCredentialsModel employeeModel) {
        if(!id.equals(employeeModel.getId())) throw new ValidationException("Path ID and Request ID not matching");
        EmployeeCredentials employeeCredentials = employeeCredentialsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        BeanUtils.copyProperties(employeeModel, employeeCredentials, "id");
        employeeCredentialsRepository.save(employeeCredentials);
    }

    public void updateVisitor(String id, @Valid UserCredentialsModel visitorModel) {
        if(!id.equals(visitorModel.getId())) throw new ValidationException("Path ID and Request ID not matching");
        VisitorCredentials visitorCredentials = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        BeanUtils.copyProperties(visitorModel, visitorCredentials, "id");
        visitorRepository.save(visitorCredentials);
    }
}
