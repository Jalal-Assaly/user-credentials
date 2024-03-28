package org.pacs.usercredentialsapi;

import org.pacs.usercredentialsapi.documents.EmployeeCredentials;

import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.models.mappers.UserMapper;
import org.pacs.usercredentialsapi.repositories.EmployeeCredentialsRepository;
import org.pacs.usercredentialsapi.services.UserCredentialsService;
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
public class UserCredentialsServiceTest {

    @Mock
    private EmployeeCredentialsRepository employeeCredentialsRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserCredentialsService employeeService;

    // Some properties
    EmployeeCredentials testEmployee1Credentials;
    EmployeeCredentials testEmployee2Credentials;
    List<EmployeeCredentials> testEmployeeCredentials;
    UserCredentialsModel testEmployeeModel1;
    UserCredentialsModel testEmployeeModel2;

    @BeforeEach void init() {
        // Initial Setup - Employee Entities
        testEmployee1Credentials = new EmployeeCredentials("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testEmployee2Credentials = new EmployeeCredentials("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Employee Models
        testEmployeeModel1 = new UserCredentialsModel("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testEmployeeModel2 = new UserCredentialsModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Employees List
        testEmployeeCredentials = Arrays.asList(testEmployee1Credentials, testEmployee2Credentials);
    }

    @Test void testGetAllEmployees() {
        // Arrange
        when(employeeCredentialsRepository.findAll()).thenReturn(testEmployeeCredentials);
        when(userMapper.toUserModel(testEmployee1Credentials)).thenReturn(testEmployeeModel1);
        when(userMapper.toUserModel(testEmployee2Credentials)).thenReturn(testEmployeeModel2);

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
        when(employeeCredentialsRepository.findById(testEmployee1Credentials.getId())).thenReturn(Optional.of(testEmployee1Credentials));
        when(userMapper.toUserModel(testEmployee1Credentials)).thenReturn(testEmployeeModel1);

        // Act
        UserCredentialsModel actualEmployee = employeeService.findEmployeeById(testEmployee1Credentials.getId());

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

    @Test void testFindEmployeeBySsn_Success() {
        // Arrange
        when(employeeCredentialsRepository.findEmployeeByEmail(testEmployee2Credentials.getEmail())).thenReturn(Optional.of(testEmployee2Credentials));
        when(userMapper.toUserModel(testEmployee2Credentials)).thenReturn(testEmployeeModel2);

        // Act
        UserCredentialsModel actualEmployee = employeeService.findEmployeeByEmail(testEmployee2Credentials.getEmail());

        // Verify & Assert
        verify(employeeCredentialsRepository).findEmployeeByEmail(any(String.class));
        verify(userMapper).toUserModel(any(EmployeeCredentials.class));
        assertThat(actualEmployee).usingRecursiveComparison().isEqualTo(testEmployeeModel2);
    }

    @Test void testFindEmployeeBySsn_NotFound() {
        // Arrange
        when(employeeCredentialsRepository.findEmployeeByEmail("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> employeeService.findEmployeeByEmail("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");
    }

    @Test void testAddEmployee_Success() {
        // Arrange
        when(userMapper.toEmployee(testEmployeeModel1)).thenReturn(testEmployee1Credentials);
        when(employeeCredentialsRepository.existsById(testEmployee1Credentials.getId())).thenReturn(false);

        // Act
        employeeService.addNewEmployee(testEmployeeModel1);

        // Verify & Assert
        verify(userMapper).toEmployee(any(UserCredentialsModel.class));
        verify(employeeCredentialsRepository).existsById(any(String.class));
        verify(employeeCredentialsRepository).save(any(EmployeeCredentials.class));
    }

    @Test void testAddEmployee_AlreadyExists() {
        // Arrange
        when(userMapper.toEmployee(testEmployeeModel1)).thenReturn(testEmployee1Credentials);
        when(employeeCredentialsRepository.existsById(testEmployee1Credentials.getId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> employeeService.addNewEmployee(testEmployeeModel1))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Employee already exists");

        // Verify No interaction
        verify(employeeCredentialsRepository, never()).save(any(EmployeeCredentials.class));
    }

    @Test void deleteEmployee_Success() {
        // Arrange
        when(employeeCredentialsRepository.findById(testEmployee2Credentials.getId())).thenReturn(Optional.of(testEmployee2Credentials));

        // Act
        employeeService.deleteEmployee(testEmployee2Credentials.getId());

        // Verify & Assert
        verify(employeeCredentialsRepository).findById(testEmployee2Credentials.getId());
        verify(employeeCredentialsRepository).delete(testEmployee2Credentials);
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
        UserCredentialsModel updatedEmployeeModel = new UserCredentialsModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "newPassword321");
        when(employeeCredentialsRepository.findById(testEmployee2Credentials.getId())).thenReturn(Optional.of(testEmployee2Credentials));

        // Act
        employeeService.updateEmployee(testEmployee2Credentials.getId(), updatedEmployeeModel);

        // Verify & Assert
        verify(employeeCredentialsRepository).findById(any(String.class));
        verify(employeeCredentialsRepository).save(any(EmployeeCredentials.class));
        assertThat(testEmployee2Credentials.getPassword()).isEqualTo(updatedEmployeeModel.getPassword());
    }

    @Test void updateEmployee_NotFound() {
        // Arrange
        UserCredentialsModel updatedEmployeeModel = new UserCredentialsModel("xyz", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "newPassword321");
        when(employeeCredentialsRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.updateEmployee("xyz", updatedEmployeeModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee was not found");

        // Verify No interaction
        verify(employeeCredentialsRepository, never()).save(any(EmployeeCredentials.class));
    }
}
