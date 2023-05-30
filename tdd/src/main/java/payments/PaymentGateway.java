package payments;

public interface PaymentGateway {
    public PaymentResponse requestPayment (PaymentRequest paymentRequest);
}
