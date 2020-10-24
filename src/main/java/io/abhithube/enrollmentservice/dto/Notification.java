package io.abhithube.enrollmentservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Notification {
    private UUID id;
    private String title;
    private String description;
    private long createdAt;
}
