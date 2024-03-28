package com.example.usercredentials.services;

import com.example.usercredentials.documents.Employee;
import com.example.usercredentials.documents.Visitor;
import com.example.usercredentials.models.UserModel;
import com.example.usercredentials.models.mappers.UserMapper;
import com.example.usercredentials.repositories.EmployeeRepository;
import com.example.usercredentials.repositories.VisitorRepository;
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
public class UserService {

    private final EmployeeRepository employeeRepository;
    private final VisitorRepository visitorRepository;
    private final UserMapper userMapper;

    public List<UserModel> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(userMapper::toUserModel)
                .toList();
    }

    public List<UserModel> getAllVisitors() {
        return visitorRepository.findAll().stream()
                .map(userMapper::toUserModel)
                .toList();
    }

    public UserModel findEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        return userMapper.toUserModel(employee);
    }

    public UserModel findVisitorById(String id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        return userMapper.toUserModel(visitor);
    }

    public UserModel findEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findEmployeeByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        return userMapper.toUserModel(employee);
    }

    public UserModel findVisitorByEmail(String email) {
        Visitor visitor = visitorRepository.findVisitorByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        return userMapper.toUserModel(visitor);
    }

    public void addNewEmployee(@Valid UserModel employeeModel) {
        Employee employee = userMapper.toEmployee(employeeModel);
        if(employeeRepository.existsById(employee.getId())) {
            throw new EntityExistsException("Employee already exists");
        } else {
            employeeRepository.save(employee);
        }
    }

    public void addNewVisitor(@Valid UserModel visitorModel) {
        Visitor visitor = userMapper.toVisitor(visitorModel);
        if(visitorRepository.existsById(visitor.getId())) {
            throw new EntityExistsException("Visitor already exists");
        } else {
            visitorRepository.save(visitor);
        }
    }

    public void deleteEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        employeeRepository.delete(employee);
    }

    public void deleteVisitor(String id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        visitorRepository.delete(visitor);
    }

    public void updateEmployee(String id, @Valid UserModel employeeModel) {
        if(!id.equals(employeeModel.getId())) throw new ValidationException("Path ID and Request ID not matching");
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee was not found"));
        BeanUtils.copyProperties(employeeModel, employee, "id");
        employeeRepository.save(employee);
    }

    public void updateVisitor(String id, @Valid UserModel visitorModel) {
        if(!id.equals(visitorModel.getId())) throw new ValidationException("Path ID and Request ID not matching");
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        BeanUtils.copyProperties(visitorModel, visitor, "id");
        visitorRepository.save(visitor);
    }
}
