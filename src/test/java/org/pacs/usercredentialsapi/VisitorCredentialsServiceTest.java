package org.pacs.usercredentialsapi;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pacs.usercredentialsapi.documents.VisitorCredentials;
import org.pacs.usercredentialsapi.models.UserCredentialsModel;
import org.pacs.usercredentialsapi.models.mappers.UserMapper;
import org.pacs.usercredentialsapi.repositories.VisitorCredentialsRepository;
import org.pacs.usercredentialsapi.services.VisitorCredentialsService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisitorCredentialsServiceTest {

    @Mock
    private VisitorCredentialsRepository visitorCredentialsRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private VisitorCredentialsService visitorService;

    // Some properties
    VisitorCredentials testVisitor1;
    VisitorCredentials testVisitor2;
    List<VisitorCredentials> testVisitorCredentials;
    UserCredentialsModel testVisitorModel1;
    UserCredentialsModel testVisitorModel2;

    @BeforeEach
    void init() {
        // Initial Setup - Visitor Entities
        testVisitor1 = new VisitorCredentials("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testVisitor2 = new VisitorCredentials("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Visitor Models
        testVisitorModel1 = new UserCredentialsModel("3", "432-33-3425", "Mike", "Johns", "mike.johns@example.com", "test234");
        testVisitorModel2 = new UserCredentialsModel("53", "543-93-5322", "Laury", "Parker", "laury.parker@example.com", "trialError432");
        // Initial Setup - Visitors List
        testVisitorCredentials = Arrays.asList(testVisitor1, testVisitor2);
    }

    @Test
    void testGetAllVisitors() {
        // Arrange
        when(visitorCredentialsRepository.findAll()).thenReturn(testVisitorCredentials);
        when(userMapper.toUserModel(testVisitor1)).thenReturn(testVisitorModel1);
        when(userMapper.toUserModel(testVisitor2)).thenReturn(testVisitorModel2);

        // Act
        List<UserCredentialsModel> actualVisitors = visitorService.getAllVisitors();

        // Verify & Assert
        verify(visitorCredentialsRepository).findAll();  // Verify 'findAll' on the repository was called
        verify(userMapper, times(2)).toUserModel(any(VisitorCredentials.class));
        assertThat(actualVisitors).hasSize(2);
        assertThat(actualVisitors.get(0)).usingRecursiveComparison().isEqualTo(testVisitorModel1);
        assertThat(actualVisitors.get(1)).usingRecursiveComparison().isEqualTo(testVisitorModel2);
    }

    @Test void testFindVisitorById_Success() {
        // Arrange
        when(visitorCredentialsRepository.findById(testVisitor1.getId())).thenReturn(Optional.of(testVisitor1));
        when(userMapper.toUserModel(testVisitor1)).thenReturn(testVisitorModel1);

        // Act
        UserCredentialsModel actualVisitor = visitorService.findVisitorById(testVisitor1.getId());

        // Verify & Assert
        verify(visitorCredentialsRepository).findById(any(String.class));
        verify(userMapper).toUserModel(any(VisitorCredentials.class));
        assertThat(actualVisitor).usingRecursiveComparison().isEqualTo(testVisitorModel1);
    }

    @Test void testFindVisitorById_NotFound() {
        // Arrange
        when(visitorCredentialsRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> visitorService.findVisitorById("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Visitor was not found");
    }

    @Test void testFindVisitorByEmail_Success() {
        // Arrange
        when(visitorCredentialsRepository.findVisitorByEmail(testVisitor2.getEmail())).thenReturn(Optional.of(testVisitor2));
        when(userMapper.toUserModel(testVisitor2)).thenReturn(testVisitorModel2);

        // Act
        UserCredentialsModel actualVisitor = visitorService.findVisitorByEmail(testVisitor2.getEmail());

        // Verify & Assert
        verify(visitorCredentialsRepository).findVisitorByEmail(any(String.class));
        verify(userMapper).toUserModel(any(VisitorCredentials.class));
        assertThat(actualVisitor).usingRecursiveComparison().isEqualTo(testVisitorModel2);
    }

    @Test void testFindVisitorByEmail_NotFound() {
        // Arrange
        when(visitorCredentialsRepository.findVisitorByEmail("xyz")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> visitorService.findVisitorByEmail("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Visitor was not found");
    }

    @Test void testAddVisitor_Success() {
        // Arrange
        when(userMapper.toVisitor(testVisitorModel1)).thenReturn(testVisitor1);
        when(visitorCredentialsRepository.existsById(testVisitor1.getId())).thenReturn(false);

        // Act
        visitorService.addNewVisitor(testVisitorModel1);

        // Verify & Assert
        verify(userMapper).toVisitor(any(UserCredentialsModel.class));
        verify(visitorCredentialsRepository).existsById(any(String.class));
        verify(visitorCredentialsRepository).insert(any(VisitorCredentials.class));
    }

    @Test void testAddVisitor_AlreadyExists() {
        // Arrange
        when(userMapper.toVisitor(testVisitorModel1)).thenReturn(testVisitor1);
        when(visitorCredentialsRepository.existsById(testVisitor1.getId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> visitorService.addNewVisitor(testVisitorModel1))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Visitor already exists");

        // Verify No interaction
        verify(visitorCredentialsRepository, never()).insert(any(VisitorCredentials.class));
    }

    @Test void deleteVisitor_Success() {
        // Arrange
        when(visitorCredentialsRepository.findById(testVisitor2.getId())).thenReturn(Optional.of(testVisitor2));

        // Act
        visitorService.deleteVisitor(testVisitor2.getId());

        // Verify & Assert
        verify(visitorCredentialsRepository).findById(testVisitor2.getId());
        verify(visitorCredentialsRepository).delete(testVisitor2);
    }

    @Test void deleteVisitor_NotFound() {
        // Arrange
        when(visitorCredentialsRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> visitorService.deleteVisitor("xyz"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Visitor was not found");

        //Verify
        verify(visitorCredentialsRepository, never()).delete(any(VisitorCredentials.class));
    }

    @Test void updateVisitor_Success() {
        // Arrange
        UserCredentialsModel updatedVisitorModel = new UserCredentialsModel(
                "53",
                "543-93-5322",
                "Laury",
                "Parker",
                "laury.parker@example.com",
                "newPassword321");
        when(visitorCredentialsRepository.findById(testVisitor2.getId())).thenReturn(Optional.of(testVisitor2));

        // Act
        visitorService.updateVisitor("53", updatedVisitorModel);

        // Verify & Assert
        verify(visitorCredentialsRepository).findById(any(String.class));
        verify(visitorCredentialsRepository).save(any(VisitorCredentials.class));
        assertThat(testVisitor2.getPassword()).isEqualTo(updatedVisitorModel.getPassword());
    }

    @Test void updateVisitor_NotFound() {
        // Arrange
        UserCredentialsModel updatedVisitorModel = new UserCredentialsModel(
                "xyz",
                "543-93-5322",
                "Laury",
                "Parker",
                "laury.parker@example.com",
                "newPassword321"); // changed
        when(visitorCredentialsRepository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> visitorService.updateVisitor("xyz", updatedVisitorModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Visitor was not found");

        // Verify No interaction
        verify(visitorCredentialsRepository, never()).save(any(VisitorCredentials.class));
    }

    @Test void updateVisitor_IdPathRequestMismatch() {
        // Arrange
        UserCredentialsModel updatedUserCredentialsModel = new UserCredentialsModel(
                "xyz",
                "543-93-5322",
                "Laury",
                "Parker",
                "laury.parker@example.com",
                "newPassword321");

        // Act & Assert
        assertThatThrownBy(() -> visitorService.updateVisitor("53", updatedUserCredentialsModel))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Path ID and Request ID not matching");

        // Verify No interaction
        verify(visitorCredentialsRepository, never()).save(any(VisitorCredentials.class));
    }
}
