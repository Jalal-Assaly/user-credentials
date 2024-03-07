package com.example.usercredentials;

import com.example.usercredentials.documents.User;
import com.example.usercredentials.models.UserDTO;
import com.example.usercredentials.models.mappers.UserMapper;
import com.example.usercredentials.repositories.UserRepository;
import com.example.usercredentials.services.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private static List<User> mockUsers;
    private static List<UserDTO> mockUserDTOs;

    @BeforeAll
    static void setupData() {
        mockUsers = List.of(new User("1", "19483028430112", "Emily", "Davis", "emilydavis@email.com", "password123"),
                new User("2", "43892738573829", "Mike", "Lans", "mikelans@gmail.com", "123456A#78"));

        mockUserDTOs = List.of(new UserDTO("1", "19483028430112", "Emily", "Davis", "emilydavis@email.com", "password123"),
                new UserDTO("2", "43892738573829", "Mike", "Lans", "mikelans@gmail.com", "123456A#78"));
    }

    @Test
    void getAllUsers_success() {

        // Arrange
        when(userRepository.findAll()).thenReturn(mockUsers);
        when(userMapper.toUserDTO(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new UserDTO(user.getId(), user.getSsn(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getPassword());
        });

        // Act
        List<UserDTO> actualUserDTOs = userService.getAllUsers();

        // Assertions
        assertThat(actualUserDTOs).isEqualTo(mockUserDTOs);
        verify(userRepository).findAll();
        verify(userMapper, times(mockUsers.size())).toUserDTO(any(User.class));
    }

    @Test
    void findUserById_success() {
        // Arrange
        User mockUser = mockUsers.get(1);
        String userId = mockUser.getId();
        UserDTO expectedUserDTO = mockUserDTOs.get(1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toUserDTO(mockUser)).thenReturn(expectedUserDTO);

        // Act
        UserDTO actualUserDTO = userService.findUserById(userId);

        // Assert
        assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
        verify(userRepository).findById(userId);
        verify(userMapper).toUserDTO(mockUser);
    }

    @Test
    void findUserById_notFound() {
        // Arrange
        String nonExistentId = "99";
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findUserById(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User was not found");
        verify(userRepository).findById(nonExistentId);
    }

    @Test
    void findUserBySsn_success() {
        // Arrange
        User mockUser = mockUsers.get(1);
        String userSsn = mockUser.getSsn();
        UserDTO expectedUserDTO = mockUserDTOs.get(1);

        when(userRepository.findUserBySsn(userSsn)).thenReturn(Optional.of(mockUser));
        when(userMapper.toUserDTO(mockUser)).thenReturn(expectedUserDTO);

        // Act
        UserDTO actualUserDTO = userService.findUserBySsn(userSsn);

        // Assert
        assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
        verify(userRepository).findUserBySsn(userSsn);
        verify(userMapper).toUserDTO(mockUser);
    }

    @Test
    void findUserBySsn_notFound() {
        // Arrange
        String nonExistentSsn = "32324141243213";
        when(userRepository.findUserBySsn(nonExistentSsn)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findUserBySsn(nonExistentSsn))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User was not found");
        verify(userRepository).findUserBySsn(nonExistentSsn);
    }

    @Test
    void addUser_success() {
        // Arrange
        User newUser = new User("1", "19483028430112", "Brandon", "Davis", "emilydavis@email.com", "password123");
        UserDTO newUserDTO = new UserDTO("1", "19483028430112", "Brandon", "Davis", "emilydavis@email.com", "password123");

        when(userRepository.existsById(newUser.getId())).thenReturn(false);
        when(userMapper.toUserDTO(any(User.class))).thenReturn(newUserDTO);

        // Act
        userService.addUser(newUserDTO);

        // Assert
        verify(userRepository).existsById(newUser.getId());
        verify(userRepository).save(newUser);
    }

    @Test
    void addUser_alreadyExists() {
        // Arrange
        User existingUser = new User("1", "19483028430112", "Emily", "Davis", "emilydavis@email.com", "password123");
        UserDTO existingUserDTO = new UserDTO("1", "19483028430112", "Emily", "Davis", "emilydavis@email.com", "password123");

        when(userRepository.existsById(existingUser.getId())).thenReturn(true);
        when(userMapper.toUserDTO(any(User.class))).thenReturn(existingUserDTO);

        // Act & Assert
        assertThatThrownBy(() -> userService.addUser(existingUserDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User already exists");
        verify(userRepository).existsById(existingUser.getId());
    }
}
