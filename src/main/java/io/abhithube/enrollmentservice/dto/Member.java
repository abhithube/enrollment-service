package io.abhithube.enrollmentservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Member {
    private String id;
    private String username;
    private String password;
    private String email;

    private Plan plan;
    private long memberSince;
    private long nextPaymentDate;
    private List<Payment> payments = new ArrayList<>();

    private List<Notification> notifications;

    private String customerId;
    private String subscriptionId;

    public void addToPayments(Payment payment) {
        payments.add(0, payment);
    }
}
