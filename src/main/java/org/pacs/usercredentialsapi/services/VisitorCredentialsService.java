package org.pacs.usercredentialsapi.services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.pacs.usercredentialsapi.documents.VisitorCredentials;
import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.models.mappers.UserMapper;
import org.pacs.usercredentialsapi.repositories.VisitorCredentialsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorCredentialsService {

    private final VisitorCredentialsRepository visitorRepository;
    private final UserMapper userMapper;

    public List<UserCredentialsModel> getAllVisitors() {
        return visitorRepository.findAll().stream()
                .map(userMapper::toUserModel)
                .toList();
    }

    public UserCredentialsModel findVisitorById(String id) {
        VisitorCredentials visitorCredentials = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        return userMapper.toUserModel(visitorCredentials);
    }

    public UserCredentialsModel findVisitorByEmail(String email) {
        VisitorCredentials visitorCredentials = visitorRepository.findVisitorByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        return userMapper.toUserModel(visitorCredentials);
    }

    public void addNewVisitor(@Valid UserCredentialsModel visitorModel) {
        VisitorCredentials visitorCredentials = userMapper.toVisitor(visitorModel);
        if(visitorRepository.existsById(visitorCredentials.getId())) {
            throw new EntityExistsException("Visitor already exists");
        } else {
            visitorRepository.insert(visitorCredentials);
        }
    }

    public void deleteVisitor(String id) {
        VisitorCredentials visitorCredentials = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        visitorRepository.delete(visitorCredentials);
    }

    public void updateVisitor(String id, @Valid UserCredentialsModel visitorModel) {
        if(!id.equals(visitorModel.getId())) throw new ValidationException("Path ID and Request ID not matching");
        VisitorCredentials visitorCredentials = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor was not found"));
        BeanUtils.copyProperties(visitorModel, visitorCredentials, "id");
        visitorRepository.save(visitorCredentials);
    }
}
