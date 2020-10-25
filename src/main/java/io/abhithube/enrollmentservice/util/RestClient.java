package io.abhithube.enrollmentservice.util;

import io.abhithube.enrollmentservice.dto.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {
    private final RestTemplate restTemplate;
    @Value("${client.members.url}")
    private String baseUrl;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Member> getMember(String username) {
        return restTemplate.getForEntity(baseUrl + username, Member.class);
    }

    public ResponseEntity<Member> getCustomer(String customerId) {
        return restTemplate.getForEntity(baseUrl + "customer/" + customerId, Member.class);
    }

    public void updateMember(Member member) {
        restTemplate.exchange(baseUrl + member.getUsername(), HttpMethod.PUT,
                new HttpEntity<>(member), Member.class);
    }
}