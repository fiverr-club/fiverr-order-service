package club.fiverr.order.application.dto.command;

import java.math.BigDecimal;
import java.util.UUID;

public record PlaceOrderCommand(
        UUID gigId,
        UUID buyerId,
        UUID sellerId,
        String packageTier,
        BigDecimal amount,
        String requirements,
        int maxRevisions
) {
}
