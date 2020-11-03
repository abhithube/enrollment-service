package io.abhithube.enrollmentservice.service;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import io.abhithube.enrollmentservice.dto.EnrollmentRequest;
import io.abhithube.enrollmentservice.dto.Member;
import io.abhithube.enrollmentservice.dto.Payment;
import io.abhithube.enrollmentservice.dto.Plan;
import io.abhithube.enrollmentservice.exception.CustomStripeException;
import io.abhithube.enrollmentservice.util.KafkaClient;
import io.abhithube.enrollmentservice.util.RestClient;
import io.abhithube.enrollmentservice.util.StripeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {
    private final StripeUtil stripeUtil;
    private final RestClient restClient;
    private final KafkaClient kafkaClient;

    @Autowired
    public EnrollmentService(StripeUtil stripeUtil, RestClient restClient, KafkaClient kafkaClient) {
        this.stripeUtil = stripeUtil;
        this.restClient = restClient;
        this.kafkaClient = kafkaClient;
    }

    public ResponseEntity<Member> createEnrollment(EnrollmentRequest enrollmentRequest) throws CustomStripeException {
        String username = enrollmentRequest.getUsername();
        String sourceId = enrollmentRequest.getSourceId();
        Plan plan = enrollmentRequest.getPlan();

        ResponseEntity<Member> toUpdate = restClient.getMember(username);
        Member member = toUpdate.getBody();
        assert member != null;

        String customerId = member.getCustomerId();

        if (customerId == null) {
            Customer customer = stripeUtil.createCustomer(member.getEmail(), sourceId);
            customerId = customer.getId();
        }

        Subscription subscription = stripeUtil.createSubscription(customerId, plan.getPriceId());

        member.setPlan(plan);
        member.setCustomerId(customerId);
        member.setSubscriptionId(subscription.getId());
        member.setMemberSince(subscription.getCurrentPeriodStart());

        ResponseEntity<Member> updated = restClient.updateMember(member);
        if (updated != null && updated.getStatusCode() == HttpStatus.OK)
            kafkaClient.publish(updated.getBody(), "enrollment");

        return updated;
    }

    public ResponseEntity<Member> cancelEnrollment(String username) throws CustomStripeException {
        ResponseEntity<Member> toUpdate = restClient.getMember(username);
        Member member = toUpdate.getBody();
        assert member != null;

        stripeUtil.cancelSubscription(member.getSubscriptionId());

        member.setPlan(null);
        member.setSubscriptionId(null);
        member.setMemberSince(0);
        member.setNextPaymentDate(0);

        ResponseEntity<Member> updated = restClient.updateMember(member);
        if (updated != null && updated.getStatusCode() == HttpStatus.OK)
            kafkaClient.publish(updated.getBody(), "cancellation");

        return updated;
    }

    public void saveTransaction(String json) throws CustomStripeException, EventDataObjectDeserializationException {
        Invoice invoice = stripeUtil.extractInvoice(json);

        ResponseEntity<Member> toUpdate = restClient.getCustomer(invoice.getCustomer());
        Member member = toUpdate.getBody();
        assert member != null;

        Payment payment = new Payment();
        payment.setAmount(invoice.getAmountPaid());
        payment.setCreatedAt(invoice.getCreated());
        payment.setPlan(member.getPlan().getName());
        payment.setInvoiceId(invoice.getId());

        Subscription subscription = stripeUtil.retrieveSubscription(invoice.getSubscription());

        member.setNextPaymentDate(subscription.getCurrentPeriodEnd());
        member.addToPayments(payment);

        ResponseEntity<Member> updated = restClient.updateMember(member);
        if (updated != null && updated.getStatusCode() == HttpStatus.OK)
            kafkaClient.publish(updated.getBody(), "payment");
    }
}
