package sg.darren.unittest._01_java;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class Checkout extends Cart {

    private Float paymentAmount = 0F;
    private Float paymentDue = 0F;
    private Date paymentDate;

    public enum PaymentStatus {
        DUE, DONE
    }

    private PaymentStatus paymentStatus;

    public Float getPaymentAmount() {
        return paymentAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public Float getPaymentDue() {
        return paymentDue;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void pay(Float payment) {
        paymentAmount = payment;
        paymentDue = getTotalAmount() - paymentAmount;
        paymentDate = new Date();
    }

    public void confirmOrder() {
        if (paymentDue == 0.0F) {
            paymentStatus = PaymentStatus.DONE;
            log.info("Payment successful! Thank you for your order.");
        } else if (paymentDue > 0) {
            paymentStatus = PaymentStatus.DUE;
            log.info("Payment Failed! Remaining {} needs to be paid.", paymentDue);
        }
    }

    public void complete() {
        printCartDetails();
        confirmOrder();
    }

}
