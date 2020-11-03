package io.abhithube.enrollmentservice.controller;

import io.abhithube.enrollmentservice.dto.EnrollmentRequest;
import io.abhithube.enrollmentservice.dto.Member;
import io.abhithube.enrollmentservice.service.EnrollmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/enrollment", produces = "application/json")
@Api(tags = "Enrollment Resource", description = "Defines the operations associated with enrolling in a benefit plan")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/create")
    @ApiOperation("Enrolls a member in a benefit plan")
    public ResponseEntity<Member> createEnrollment(@ApiParam(value = "The payment details needed to charge a member " +
            "monthly for their enrolled plan") @RequestBody EnrollmentRequest enrollmentRequest) throws Exception {
        return enrollmentService.createEnrollment(enrollmentRequest);
    }

    @PostMapping("/cancel")
    @ApiOperation("Cancels a member's enrollment in a benefit plan")
    public ResponseEntity<Member> cancelEnrollment(@ApiParam(value = "The username of the member whose enrollment " +
            "will be cancelled") @RequestBody String username) throws Exception {
        return enrollmentService.cancelEnrollment(username);
    }

    @PostMapping("/save")
    @ApiOperation(value = "", hidden = true)
    public ResponseEntity<Void> saveTransaction(@RequestBody String json) throws Exception {
        enrollmentService.saveTransaction(json);
        return ResponseEntity.ok().build();
    }
}
