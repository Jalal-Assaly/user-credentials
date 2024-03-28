package org.pacs.usercredentialsapi.models.mappers;

import org.pacs.usercredentialsapi.documents.EmployeeCredentials;
import org.pacs.usercredentialsapi.documents.UserCredentials;
import org.pacs.usercredentialsapi.documents.VisitorCredentials;
import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserCredentialsModel toUserModel(UserCredentials userCredentials);
    EmployeeCredentials toEmployee(UserCredentialsModel userCredentialsModel);
    VisitorCredentials toVisitor(UserCredentialsModel userCredentialsModel);
}
