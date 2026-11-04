package club.fiverr.order.presentation.rest.response;

import club.fiverr.order.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID gigId,
        UUID buyerId,
        UUID sellerId,
        String packageTier,
        BigDecimal amount,
        String requirements,
        int maxRevisions,
        int usedRevisions,
        OrderStatus status,
        String deliveryNote,
        String deliverableUrl,
        String lastRevisionReason,
        String escrowReference,
        Instant createdAt,
        Instant updatedAt
) {
}
