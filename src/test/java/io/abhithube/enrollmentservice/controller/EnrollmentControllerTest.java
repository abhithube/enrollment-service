package io.abhithube.enrollmentservice.controller;

import io.abhithube.enrollmentservice.dto.EnrollmentRequest;
import io.abhithube.enrollmentservice.dto.Member;
import io.abhithube.enrollmentservice.dto.Plan;
import io.abhithube.enrollmentservice.service.EnrollmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
        Member in = new Member();
        Plan plan = new Plan();
        plan.setName("plan123");
        in.setPlan(plan);
        when(enrollmentService.createEnrollment(any(EnrollmentRequest.class)))
                .thenReturn(ResponseEntity.ok(in));

        // Act
        ResponseEntity<Member> responseEntity = enrollmentController.createEnrollment(new EnrollmentRequest());

        // Assert
        Member out = responseEntity.getBody();
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(out);
        assertEquals("plan123", out.getPlan().getName());
    }


    @Test
    @DisplayName("it should cancel a member's enrollment")
    void cancelEnrollment() throws Exception {
        // Arrange
        Member in = new Member();
        when(enrollmentService.cancelEnrollment(anyString()))
                .thenReturn(ResponseEntity.ok(in));

        // Act
        ResponseEntity<Member> responseEntity = enrollmentController.cancelEnrollment("test");

        // Assert
        Member out = responseEntity.getBody();
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(out);
        assertNull(out.getPlan());
    }


    @Test
    @DisplayName("it should save a payment charged to a member's account")
    void saveTransaction() throws Exception {
        // Arrange
        doNothing()
                .when(enrollmentService).saveTransaction(anyString());

        // Act
        ResponseEntity<Void> responseEntity = enrollmentController.saveTransaction("test");

        // Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }
}