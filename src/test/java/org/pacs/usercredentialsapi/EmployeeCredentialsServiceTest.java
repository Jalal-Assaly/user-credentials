package org.pacs.usercredentialsapi;

import jakarta.validation.ValidationException;
import org.pacs.usercredentialsapi.documents.EmployeeCredentials;

import org.pacs.usercredentialsapi.documents.VisitorCredentials;
import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.models.mappers.UserMapper;
import org.pacs.usercredentialsapi.repositories.EmployeeCredentialsRepository;
import org.pacs.usercredentialsapi.services.EmployeeCredentialsService;
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
public class EmployeeCredentialsServiceTest {

    @Mock
    private EmployeeCredentialsRepository employeeCredentialsRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private EmployeeCredentialsService employeeService;

    // Some properties
    EmployeeCredentials testEmployee1;
    EmployeeCredentials testEmployee2;
    List<EmployeeCredentials> testEmployeeCredentials;
    UserCredentialsModel testEmployeeModel1;
    UserCredentialsModel testEmployeeModel2;

    @BeforeEach void init() {
        // Initial Setup - Employee Entities
        testEmployee1 = new EmployeeCredentials("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testEmployee2 = new EmployeeCredentials("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Employee Models
        testEmployeeModel1 = new UserCredentialsModel("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testEmployeeModel2 = new UserCredentialsModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Employees List
        testEmployeeCredentials = Arrays.asList(testEmployee1, testEmployee2);
    }

    @Test void testGetAllEmployees() {
        // Arrange
        when(employeeCredentialsRepository.findAll()).thenReturn(testEmployeeCredentials);
        when(userMapper.toUserModel(testEmployee1)).thenReturn(testEmployeeModel1);
        when(userMapper.toUserModel(testEmployee2)).thenReturn(testEmployeeModel2);

        // Act
        List<UserCredentialsModel> actualEmployees = employeeService.getAllEmployees();

        // Verify & Assert
        verify(employeeCredentialsRepository).findAll();  // Verify 'findAll' on the repository was called
        verify(userMapper, times(2)).toUserModel(any(EmployeeCredentials.class));
        assertThat(actualEmployees).hasSize(2);
        assertThat(actualEmployees.get(0)).usingRecursiveComparison().isEqualTo(testEmployeeModel1);
        assertThat(actualEmployees.get(1)).usingRecursiveComparison().isEqualTo(testEmployeeModel2);
    }

    @Test void testFindEmployeeById_Success() {
        // Arrange
        when(employeeCredentialsRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));
        when(userMapper.toUserModel(testEmployee1)).thenReturn(testEmployeeModel1);

        // Act
        UserCredentialsModel actualEmployee = employeeService.findEmployeeById(testEmployee1.getId());

        // Verify & Assert
        verify(employeeCredentialsRepository).findById(any(String.class));
        verify(userMapper).toUserModel(any(EmployeeCredentials.class));
        assertThat(actualEmployee).usingRecursiveComparison().isEqualTo(testEmployeeModel1);
    }

    @Test void testFindEmployeeById_NotFound() {
        // Arrange
        when(employeeCredentialsRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> employeeService.findEmployeeById("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");
    }

    @Test void testFindEmployeeByEmail_Success() {
        // Arrange
        when(employeeCredentialsRepository.findEmployeeByEmail(testEmployee2.getEmail())).thenReturn(Optional.of(testEmployee2));
        when(userMapper.toUserModel(testEmployee2)).thenReturn(testEmployeeModel2);

        // Act
        UserCredentialsModel actualEmployee = employeeService.findEmployeeByEmail(testEmployee2.getEmail());

        // Verify & Assert
        verify(employeeCredentialsRepository).findEmployeeByEmail(any(String.class));
        verify(userMapper).toUserModel(any(EmployeeCredentials.class));
        assertThat(actualEmployee).usingRecursiveComparison().isEqualTo(testEmployeeModel2);
    }

    @Test void testFindEmployeeByEmail_NotFound() {
        // Arrange
        when(employeeCredentialsRepository.findEmployeeByEmail("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> employeeService.findEmployeeByEmail("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");
    }

    @Test void testAddEmployee_Success() {
        // Arrange
        when(userMapper.toEmployee(testEmployeeModel1)).thenReturn(testEmployee1);
        when(employeeCredentialsRepository.existsById(testEmployee1.getId())).thenReturn(false);

        // Act
        employeeService.addNewEmployee(testEmployeeModel1);

        // Verify & Assert
        verify(userMapper).toEmployee(any(UserCredentialsModel.class));
        verify(employeeCredentialsRepository).existsById(any(String.class));
        verify(employeeCredentialsRepository).save(any(EmployeeCredentials.class));
    }

    @Test void testAddEmployee_AlreadyExists() {
        // Arrange
        when(userMapper.toEmployee(testEmployeeModel1)).thenReturn(testEmployee1);
        when(employeeCredentialsRepository.existsById(testEmployee1.getId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> employeeService.addNewEmployee(testEmployeeModel1))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Employee already exists");

        // Verify No interaction
        verify(employeeCredentialsRepository, never()).save(any(EmployeeCredentials.class));
    }

    @Test void deleteEmployee_Success() {
        // Arrange
        when(employeeCredentialsRepository.findById(testEmployee2.getId())).thenReturn(Optional.of(testEmployee2));

        // Act
        employeeService.deleteEmployee(testEmployee2.getId());

        // Verify & Assert
        verify(employeeCredentialsRepository).findById(testEmployee2.getId());
        verify(employeeCredentialsRepository).delete(testEmployee2);
    }

    @Test void deleteEmployee_NotFound() {
        // Arrange
        when(employeeCredentialsRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.deleteEmployee("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");

        //Verify
        verify(employeeCredentialsRepository, never()).delete(any(EmployeeCredentials.class));
    }

    @Test void updateEmployee_Success() {
        // Arrange
        UserCredentialsModel updatedEmployeeModel = new UserCredentialsModel(
                "53",
                "543-93-5322",
                "Laury",
                "Parker",
                "laury.parker@example.com",
                "newPassword321");
        when(employeeCredentialsRepository.findById(testEmployee2.getId())).thenReturn(Optional.of(testEmployee2));

        // Act
        employeeService.updateEmployee(testEmployee2.getId(), updatedEmployeeModel);

        // Verify & Assert
        verify(employeeCredentialsRepository).findById(any(String.class));
        verify(employeeCredentialsRepository).save(any(EmployeeCredentials.class));
        assertThat(testEmployee2.getPassword()).isEqualTo(updatedEmployeeModel.getPassword());
    }

    @Test void updateEmployee_NotFound() {
        // Arrange
        UserCredentialsModel updatedEmployeeModel = new UserCredentialsModel(
                "xyz",
                "543-93-5322",
                "Laury",
                "Parker",
                "laury.parker@example.com",
                "newPassword321");
        when(employeeCredentialsRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.updateEmployee("xyz", updatedEmployeeModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");

        // Verify No interaction
        verify(employeeCredentialsRepository, never()).save(any(EmployeeCredentials.class));
    }

    @Test void updateEmployee_IdPathRequestMismatch() {
        // Arrange
        UserCredentialsModel updatedEmployeeModel = new UserCredentialsModel(
                "xyz",
                "543-93-5322",
                "Laury",
                "Parker",
                "laury.parker@example.com",
                "newPassword321");

        // Act & Assert
        assertThatThrownBy(() -> employeeService.updateEmployee("53", updatedEmployeeModel))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Path ID and Request ID not matching");

        // Verify No interaction
        verify(employeeCredentialsRepository, never()).save(any(EmployeeCredentials.class));
    }
}
