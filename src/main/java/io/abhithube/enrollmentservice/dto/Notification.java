package io.abhithube.enrollmentservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Notification {
    private String id;
    private String title;
    private String description;
    private long createdAt;
}
