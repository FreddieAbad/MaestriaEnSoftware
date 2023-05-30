package payments;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class PaymentProcessorTest {
    private PaymentProcessor paymentProcessor=null;
    private PaymentGateway paymentGateway=null;
    @Before
    public void setUp(){
        paymentGateway=Mockito.mock(PaymentGateway.class);
        paymentProcessor=new PaymentProcessor(paymentGateway);
    }

    @Test
    public void give_payment_when_is_correct_then_ok() {
        //PaymentGateway paymentGateway= Mockito.mock(PaymentGateway.class);
        //PaymentProcessor paymentProcessor=new PaymentProcessor(paymentGateway);
        Mockito.when(paymentGateway.requestPayment(Mockito.any()))
                .thenReturn(new PaymentResponse(PaymentResponse.PaymentStatus.OK));

        assertTrue(paymentProcessor.makePayment(100));
    }

    @Test
    public void give_payment_when_is_wrong_then_error() {
        //PaymentGateway paymentGateway= Mockito.mock(PaymentGateway.class);
        //PaymentProcessor paymentProcessor=new PaymentProcessor(paymentGateway);
        Mockito.when(paymentGateway.requestPayment(Mockito.any()))
                .thenReturn(new PaymentResponse(PaymentResponse.PaymentStatus.ERROR));

        assertFalse(paymentProcessor.makePayment(100));
    }
}