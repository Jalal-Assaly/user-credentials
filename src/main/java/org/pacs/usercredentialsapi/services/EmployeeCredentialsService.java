package org.pacs.usercredentialsapi.services;

import org.pacs.usercredentialsapi.documents.EmployeeCredentials;
import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.models.mappers.UserMapper;
import org.pacs.usercredentialsapi.repositories.EmployeeCredentialsRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class EmployeeCredentialsService {

    private final EmployeeCredentialsRepository employeeCredentialsRepository;
    private final UserMapper userMapper;

    public List<UserCredentialsModel> getAllEmployees() {
        return employeeCredentialsRepository.findAll()
                .stream()
                .map(userMapper::toUserModel)
                .toList();
    }

    public UserCredentialsModel findEmployeeById(String id) {
        EmployeeCredentials employeeCredentials = employeeCredentialsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        return userMapper.toUserModel(employeeCredentials);
    }

    public UserCredentialsModel findEmployeeByEmail(String email) {
        EmployeeCredentials employeeCredentials = employeeCredentialsRepository.findEmployeeByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        return userMapper.toUserModel(employeeCredentials);
    }

    public void addNewEmployee(@Valid UserCredentialsModel employeeModel) {
        EmployeeCredentials employeeCredentials = userMapper.toEmployee(employeeModel);
        if(employeeCredentialsRepository.existsById(employeeCredentials.getId())) {
            throw new EntityExistsException("Employee already exists");
        } else {
            employeeCredentialsRepository.save(employeeCredentials);
        }
    }

    public void deleteEmployee(String id) {
        EmployeeCredentials employeeCredentials = employeeCredentialsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        employeeCredentialsRepository.delete(employeeCredentials);
    }

    public void updateEmployee(String id, @Valid UserCredentialsModel employeeModel) {
        if(!id.equals(employeeModel.getId())) throw new ValidationException("Path ID and Request ID not matching");
        EmployeeCredentials employeeCredentials = employeeCredentialsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        BeanUtils.copyProperties(employeeModel, employeeCredentials, "id");
        employeeCredentialsRepository.save(employeeCredentials);
    }
}
