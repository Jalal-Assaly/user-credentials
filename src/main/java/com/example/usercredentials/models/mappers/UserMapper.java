package com.example.usercredentials.models.mappers;

import com.example.usercredentials.documents.User;
import com.example.usercredentials.models.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDTO userDTO);
    UserDTO toUserDTO(User user);
}
