package com.example.usercredentials;

import com.example.usercredentials.documents.User;
import com.example.usercredentials.models.mappers.UserMapper;
import com.example.usercredentials.repositories.UserRepository;
import com.example.usercredentials.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    static void setupData() {
        mockUsers = List.of(new User(""),
                new User("2", "43892738573829", "Mike", "Lans", "mikelans@gmail.com", "123456A#78"));

        // Assuming you have a conversion method in the UserMapper
        mockUserDTOs = mockUsers.stream()
                .map(userMapper::toUserDTO)
                .toList();
    }
}
