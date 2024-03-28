package com.example.usercredentials.models.mappers;

import com.example.usercredentials.documents.Employee;
import com.example.usercredentials.documents.User;
import com.example.usercredentials.documents.Visitor;
import com.example.usercredentials.models.UserModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserModel toUserModel(User user);
    Employee toEmployee(UserModel userModel);
    Visitor toVisitor(UserModel userModel);
}
