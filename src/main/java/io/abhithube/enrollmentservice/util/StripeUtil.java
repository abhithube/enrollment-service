package io.abhithube.enrollmentservice.util;

import com.stripe.Stripe;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import io.abhithube.enrollmentservice.exception.CustomStripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.stripe.net.ApiResource.GSON;

@Component
public class StripeUtil {
    @Value("${stripe.api.key}")
    private String STRIPE_API_KEY;

    public Customer createCustomer(String email, String sourceId) throws CustomStripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                .setEmail(email)
                .setSource(sourceId)
                .build();

        try {
            return Customer.create(customerCreateParams);
        } catch (StripeException e) {
            throw new CustomStripeException(e.getMessage(), e.getRequestId(), e.getCode(), e.getStatusCode());
        }
    }

    public Subscription createSubscription(String customerId, String priceId) throws CustomStripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        SubscriptionCreateParams subscriptionCreateParams = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(priceId)
                        .build())
                .build();

        try {
            return Subscription.create(subscriptionCreateParams);
        } catch (StripeException e) {
            throw new CustomStripeException(e.getMessage(), e.getRequestId(), e.getCode(), e.getStatusCode());
        }
    }

    public Subscription retrieveSubscription(String subscriptionId) throws CustomStripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        try {
            return Subscription.retrieve(subscriptionId);
        } catch (StripeException e) {
            throw new CustomStripeException(e.getMessage(), e.getRequestId(), e.getCode(), e.getStatusCode());
        }
    }

    public void cancelSubscription(String subscriptionId) throws CustomStripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        try {
            Subscription.retrieve(subscriptionId)
                    .cancel();
        } catch (StripeException e) {
            throw new CustomStripeException(e.getMessage(), e.getRequestId(), e.getCode(), e.getStatusCode());
        }
    }

    public Invoice extractInvoice(String json) throws EventDataObjectDeserializationException {
        Event event = GSON.fromJson(json, Event.class);

        return (Invoice) event.getDataObjectDeserializer().deserializeUnsafe();
    }
}
