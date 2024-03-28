package com.example.usercredentials;

import com.example.usercredentials.documents.Employee;

import com.example.usercredentials.models.UserModel;
import com.example.usercredentials.models.mappers.UserMapper;
import com.example.usercredentials.repositories.EmployeeRepository;
import com.example.usercredentials.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService employeeService;

    // Some properties
    Employee testEmployee1;
    Employee testEmployee2;
    List<Employee> testEmployees;
    UserModel testEmployeeModel1;
    UserModel testEmployeeModel2;

    @BeforeEach void init() {
        // Initial Setup - Employee Entities
        testEmployee1 = new Employee("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testEmployee2 = new Employee("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Employee Models
        testEmployeeModel1 = new UserModel("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testEmployeeModel2 = new UserModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Employees List
        testEmployees = Arrays.asList(testEmployee1, testEmployee2);
    }

    @Test void testGetAllEmployees() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(testEmployees);
        when(userMapper.toUserModel(testEmployee1)).thenReturn(testEmployeeModel1);
        when(userMapper.toUserModel(testEmployee2)).thenReturn(testEmployeeModel2);

        // Act
        List<UserModel> actualEmployees = employeeService.getAllEmployees();

        // Verify & Assert
        verify(employeeRepository).findAll();  // Verify 'findAll' on the repository was called
        verify(userMapper, times(2)).toUserModel(any(Employee.class));
        assertThat(actualEmployees).hasSize(2);
        assertThat(actualEmployees.get(0)).usingRecursiveComparison().isEqualTo(testEmployeeModel1);
        assertThat(actualEmployees.get(1)).usingRecursiveComparison().isEqualTo(testEmployeeModel2);
    }

    @Test void testFindEmployeeById_Success() {
        // Arrange
        when(employeeRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));
        when(userMapper.toUserModel(testEmployee1)).thenReturn(testEmployeeModel1);

        // Act
        UserModel actualEmployee = employeeService.findEmployeeById(testEmployee1.getId());

        // Verify & Assert
        verify(employeeRepository).findById(any(String.class));
        verify(userMapper).toUserModel(any(Employee.class));
        assertThat(actualEmployee).usingRecursiveComparison().isEqualTo(testEmployeeModel1);
    }

    @Test void testFindEmployeeById_NotFound() {
        // Arrange
        when(employeeRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> employeeService.findEmployeeById("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");
    }

    @Test void testFindEmployeeBySsn_Success() {
        // Arrange
        when(employeeRepository.findEmployeeByEmail(testEmployee2.getEmail())).thenReturn(Optional.of(testEmployee2));
        when(userMapper.toUserModel(testEmployee2)).thenReturn(testEmployeeModel2);

        // Act
        UserModel actualEmployee = employeeService.findEmployeeByEmail(testEmployee2.getEmail());

        // Verify & Assert
        verify(employeeRepository).findEmployeeByEmail(any(String.class));
        verify(userMapper).toUserModel(any(Employee.class));
        assertThat(actualEmployee).usingRecursiveComparison().isEqualTo(testEmployeeModel2);
    }

    @Test void testFindEmployeeBySsn_NotFound() {
        // Arrange
        when(employeeRepository.findEmployeeByEmail("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> employeeService.findEmployeeByEmail("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");
    }

    @Test void testAddEmployee_Success() {
        // Arrange
        when(userMapper.toEmployee(testEmployeeModel1)).thenReturn(testEmployee1);
        when(employeeRepository.existsById(testEmployee1.getId())).thenReturn(false);

        // Act
        employeeService.addNewEmployee(testEmployeeModel1);

        // Verify & Assert
        verify(userMapper).toEmployee(any(UserModel.class));
        verify(employeeRepository).existsById(any(String.class));
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test void testAddEmployee_AlreadyExists() {
        // Arrange
        when(userMapper.toEmployee(testEmployeeModel1)).thenReturn(testEmployee1);
        when(employeeRepository.existsById(testEmployee1.getId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> employeeService.addNewEmployee(testEmployeeModel1))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Employee already exists");

        // Verify No interaction
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test void deleteEmployee_Success() {
        // Arrange
        when(employeeRepository.findById(testEmployee2.getId())).thenReturn(Optional.of(testEmployee2));

        // Act
        employeeService.deleteEmployee(testEmployee2.getId());

        // Verify & Assert
        verify(employeeRepository).findById(testEmployee2.getId());
        verify(employeeRepository).delete(testEmployee2);
    }

    @Test void deleteEmployee_NotFound() {
        // Arrange
        when(employeeRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.deleteEmployee("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");

        //Verify
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test void updateEmployee_Success() {
        // Arrange
        UserModel updatedEmployeeModel = new UserModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "newPassword321");
        when(employeeRepository.findById(testEmployee2.getId())).thenReturn(Optional.of(testEmployee2));

        // Act
        employeeService.updateEmployee(testEmployee2.getId(), updatedEmployeeModel);

        // Verify & Assert
        verify(employeeRepository).findById(any(String.class));
        verify(employeeRepository).save(any(Employee.class));
        assertThat(testEmployee2.getPassword()).isEqualTo(updatedEmployeeModel.getPassword());
    }

    @Test void updateEmployee_NotFound() {
        // Arrange
        UserModel updatedEmployeeModel = new UserModel("xyz", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "newPassword321");
        when(employeeRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.updateEmployee("xyz", updatedEmployeeModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");

        // Verify No interaction
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
