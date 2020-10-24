package io.abhithube.enrollmentservice.controller;

import io.abhithube.enrollmentservice.dto.EnrollmentRequest;
import io.abhithube.enrollmentservice.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEnrollment(@RequestBody EnrollmentRequest enrollmentRequest) throws Exception {
        enrollmentService.createEnrollment(enrollmentRequest);
        return ResponseEntity.ok("Enrollment created successfully");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelEnrollment(@RequestBody String username) throws Exception {
        enrollmentService.cancelEnrollment(username);
        return ResponseEntity.ok("Enrollment cancelled successfully");
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveTransaction(@RequestBody String json) throws Exception {
        enrollmentService.saveTransaction(json);
        return ResponseEntity.ok("Transaction saved successfully");
    }
}
