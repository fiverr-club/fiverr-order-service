package club.fiverr.order.application.port.out;

import java.math.BigDecimal;
import java.util.UUID;

public interface EscrowPaymentPort {

    String holdEscrowFunds(UUID orderId, UUID buyerId, UUID sellerId, BigDecimal amount);

    void releaseEscrowFunds(UUID orderId, String escrowReference);
}
