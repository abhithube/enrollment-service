package io.abhithube.enrollmentservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Payment {
    private String id = UUID.randomUUID().toString();
    private long amount;
    private long createdAt;
    private String plan;

    private String invoiceId;
}
