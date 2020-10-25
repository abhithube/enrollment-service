package io.abhithube.enrollmentservice.service;

import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import io.abhithube.enrollmentservice.dto.EnrollmentRequest;
import io.abhithube.enrollmentservice.dto.Member;
import io.abhithube.enrollmentservice.dto.Plan;
import io.abhithube.enrollmentservice.util.RestClient;
import io.abhithube.enrollmentservice.util.StripeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {
    @InjectMocks
    private EnrollmentService enrollmentService;
    @Mock
    private StripeUtil stripeUtil;
    @Mock
    private RestClient restClient;

    @Test
    @DisplayName("it should enroll a new customer")
    void createEnrollment1() throws Exception {
        // Arrange
        Member member = new Member();
        member.setEmail("email");
        ResponseEntity<Member> responseEntity = ResponseEntity.ok(member);
        when(restClient.getMember(anyString()))
                .thenReturn(responseEntity);

        Customer customer = new Customer();
        customer.setId("cust123");
        when(stripeUtil.createCustomer(anyString(), anyString()))
                .thenReturn(customer);

        Subscription subscription = new Subscription();
        subscription.setId("sub123");
        subscription.setCurrentPeriodStart(0L);
        when(stripeUtil.createSubscription(anyString(), anyString()))
                .thenReturn(subscription);

        doNothing()
                .when(restClient).updateMember(any(Member.class));

        // Act
        Plan plan = new Plan();
        plan.setPriceId("price123");
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest("source", "username", plan);
        enrollmentService.createEnrollment(enrollmentRequest);

        // Assert
        verify(stripeUtil).createCustomer("email", "source");
        assertEquals("sub123", member.getSubscriptionId());
        assertEquals(plan, member.getPlan());
    }

    @Test
    @DisplayName("it should enroll an existing customer")
    void createEnrollmentTest2() throws Exception {
        // Arrange
        Member member = new Member();
        member.setCustomerId("cust123");
        ResponseEntity<Member> responseEntity = ResponseEntity.ok(member);
        when(restClient.getMember(anyString()))
                .thenReturn(responseEntity);

        Subscription subscription = new Subscription();
        subscription.setId("sub123");
        subscription.setCurrentPeriodStart(0L);
        when(stripeUtil.createSubscription(anyString(), anyString()))
                .thenReturn(subscription);

        doNothing()
                .when(restClient).updateMember(any(Member.class));

        // Act
        Plan plan = new Plan();
        plan.setPriceId("price123");
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest("source", "username", plan);
        enrollmentService.createEnrollment(enrollmentRequest);

        // Assert
        verify(stripeUtil, times(0)).createCustomer(anyString(), anyString());
        assertEquals("sub123", member.getSubscriptionId());
        assertEquals(plan, member.getPlan());
    }

    @Test
    @DisplayName("it should cancel a member's enrollment")
    void cancelEnrollmentTest() throws Exception {
        // Arrange
        Member member = new Member();
        member.setSubscriptionId("sub123");
        ResponseEntity<Member> responseEntity = ResponseEntity.ok(member);
        when(restClient.getMember(anyString()))
                .thenReturn(responseEntity);

        doNothing()
                .when(stripeUtil).cancelSubscription(nullable(String.class));

        doNothing()
                .when(restClient).updateMember(any(Member.class));

        // Act
        enrollmentService.cancelEnrollment("test");

        // Assert
        verify(stripeUtil).cancelSubscription("sub123");
        assertNull(member.getSubscriptionId());
    }

    @Test
    @DisplayName("it should save a payment charged to a member's account")
    void saveTransaction() throws Exception {
        // Arrange
        Invoice invoice = new Invoice();
        invoice.setId("inv123");
        invoice.setCustomer("cust123");
        invoice.setSubscription("sub123");
        invoice.setAmountPaid(0L);
        invoice.setCreated(0L);
        when(stripeUtil.extractInvoice(anyString()))
                .thenReturn(invoice);

        Member member = new Member();
        member.setPlan(new Plan());
        ResponseEntity<Member> responseEntity = ResponseEntity.ok(member);
        when(restClient.getCustomer(anyString()))
                .thenReturn(responseEntity);

        Subscription subscription = new Subscription();
        subscription.setCurrentPeriodEnd(0L);
        when(stripeUtil.retrieveSubscription(anyString()))
                .thenReturn(subscription);

        doNothing()
                .when(restClient).updateMember(any(Member.class));

        // Act
        enrollmentService.saveTransaction("json");

        // Assert
        verify(restClient).getCustomer("cust123");
        verify(stripeUtil).retrieveSubscription("sub123");
        assertEquals(member.getPayments().size(), 1);
    }
}