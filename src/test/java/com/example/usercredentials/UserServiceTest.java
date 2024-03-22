package com.example.usercredentials;

import com.example.usercredentials.documents.User;
import com.example.usercredentials.models.UserModel;
import com.example.usercredentials.models.mappers.UserMapper;
import com.example.usercredentials.repositories.UserRepository;
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
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    // Some properties
    User testUser1;
    User testUser2;
    List<User> testUsers;
    UserModel testUserModel1;
    UserModel testUserModel2;

    @BeforeEach void init() {
        // Initial Setup - User Entities
        testUser1 = new User("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testUser2 = new User("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - User Models
        testUserModel1 = new UserModel("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testUserModel2 = new UserModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Users List
        testUsers = Arrays.asList(testUser1, testUser2);
    }

    @Test void testGetAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(testUsers);
        when(userMapper.toUserModel(testUser1)).thenReturn(testUserModel1);
        when(userMapper.toUserModel(testUser2)).thenReturn(testUserModel2);

        // Act
        List<UserModel> actualUsers = userService.getAllUsers();

        // Verify & Assert
        verify(userRepository).findAll();  // Verify 'findAll' on the repository was called
        verify(userMapper, times(2)).toUserModel(any(User.class));
        assertThat(actualUsers).hasSize(2);
        assertThat(actualUsers.get(0)).usingRecursiveComparison().isEqualTo(testUserModel1);
        assertThat(actualUsers.get(1)).usingRecursiveComparison().isEqualTo(testUserModel2);
    }

    @Test void testFindUserById_Success() {
        // Arrange
        when(userRepository.findById(testUser1.getId())).thenReturn(Optional.of(testUser1));
        when(userMapper.toUserModel(testUser1)).thenReturn(testUserModel1);

        // Act
        UserModel actualUser = userService.findUserById(testUser1.getId());

        // Verify & Assert
        verify(userRepository).findById(any(String.class));
        verify(userMapper).toUserModel(any(User.class));
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(testUserModel1);
    }

    @Test void testFindUserById_NotFound() {
        // Arrange
        when(userRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> userService.findUserById("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User was not found");
    }

    @Test void testFindUserBySsn_Success() {
        // Arrange
        when(userRepository.findUserBySsn(testUser2.getSsn())).thenReturn(Optional.of(testUser2));
        when(userMapper.toUserModel(testUser2)).thenReturn(testUserModel2);

        // Act
        UserModel actualUser = userService.findUserBySsn(testUser2.getSsn());

        // Verify & Assert
        verify(userRepository).findUserBySsn(any(String.class));
        verify(userMapper).toUserModel(any(User.class));
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(testUserModel2);
    }

    @Test void testFindUserBySsn_NotFound() {
        // Arrange
        when(userRepository.findUserBySsn("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> userService.findUserBySsn("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User was not found");
    }

    @Test void testAddUser_Success() {
        // Arrange
        when(userMapper.toUser(testUserModel1)).thenReturn(testUser1);
        when(userRepository.existsById(testUser1.getId())).thenReturn(false);

        // Act
        userService.addUser(testUserModel1);

        // Verify & Assert
        verify(userMapper).toUser(any(UserModel.class));
        verify(userRepository).existsById(any(String.class));
        verify(userRepository).save(any(User.class));
    }

    @Test void testAddUser_AlreadyExists() {
        // Arrange
        when(userMapper.toUser(testUserModel1)).thenReturn(testUser1);
        when(userRepository.existsById(testUser1.getId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.addUser(testUserModel1))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("User already exists");

        // Verify No interaction
        verify(userRepository, never()).save(any(User.class));
    }

    @Test void deleteUser_Success() {
        // Arrange
        when(userRepository.findById(testUser2.getId())).thenReturn(Optional.of(testUser2));

        // Act
        userService.deleteUser(testUser2.getId());

        // Verify & Assert
        verify(userRepository).findById(testUser2.getId());
        verify(userRepository).delete(testUser2);
    }

    @Test void deleteUser_NotFound() {
        // Arrange
        when(userRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User was not found");

        //Verify
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test void updateUser_Success() {
        // Arrange
        UserModel updatedUserModel = new UserModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "newPassword321");
        when(userRepository.findById(testUser2.getId())).thenReturn(Optional.of(testUser2));

        // Act
        userService.updateUser(testUser2.getId(), updatedUserModel);

        // Verify & Assert
        verify(userRepository).findById(any(String.class));
        verify(userRepository).save(any(User.class));
        assertThat(testUser2.getPassword()).isEqualTo(updatedUserModel.getPassword());
    }

    @Test void updateUser_NotFound() {
        // Arrange
        UserModel updatedUserModel = new UserModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "newPassword321");
        when(userRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser("xyz", updatedUserModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User was not found");

        // Verify No interaction
        verify(userRepository, never()).save(any(User.class));
    }
}
