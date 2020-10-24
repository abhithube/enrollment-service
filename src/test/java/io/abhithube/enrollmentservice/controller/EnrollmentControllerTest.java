package io.abhithube.enrollmentservice.controller;

import io.abhithube.enrollmentservice.dto.EnrollmentRequest;
import io.abhithube.enrollmentservice.service.EnrollmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {
    @InjectMocks
    private EnrollmentController enrollmentController;
    @Mock
    private EnrollmentService enrollmentService;

    @Test
    @DisplayName("it should enroll a member")
    void createEnrollment() throws Exception {
        // Arrange
        doNothing()
                .when(enrollmentService).createEnrollment(any(EnrollmentRequest.class));

        // Act
        ResponseEntity<String> responseEntity = enrollmentController.createEnrollment(new EnrollmentRequest());

        // Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }


    @Test
    @DisplayName("it should cancel a member's enrollment")
    void cancelEnrollment() throws Exception {
        // Arrange
        doNothing()
                .when(enrollmentService).cancelEnrollment(anyString());

        // Act
        ResponseEntity<String> responseEntity = enrollmentController.cancelEnrollment("test");

        // Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }


    @Test
    @DisplayName("it should save a payment charged to a member's account")
    void saveTransaction() throws Exception {
        // Arrange
        doNothing()
                .when(enrollmentService).saveTransaction(anyString());

        // Act
        ResponseEntity<String> responseEntity = enrollmentController.saveTransaction("test");

        // Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }
}