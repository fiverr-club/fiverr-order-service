package club.fiverr.order.infrastructure.adapter;

import club.fiverr.order.application.port.out.EscrowPaymentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class EscrowPaymentAdapter implements EscrowPaymentPort {

    private static final Logger log = LoggerFactory.getLogger(EscrowPaymentAdapter.class);

    @Override
    public String holdEscrowFunds(UUID orderId, UUID buyerId, UUID sellerId, BigDecimal amount) {
        String reference = "ESCROW-" + orderId.toString().substring(0, 8).toUpperCase();
        log.info("Escrow funds held via payment service: orderId={}, amount={}, escrowRef={}",
                orderId, amount, reference);
        return reference;
    }

    @Override
    public void releaseEscrowFunds(UUID orderId, String escrowReference) {
        log.info("Escrow funds released via payment service: orderId={}, escrowRef={}",
                orderId, escrowReference);
    }
}
