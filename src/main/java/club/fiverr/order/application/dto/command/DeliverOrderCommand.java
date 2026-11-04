package club.fiverr.order.application.dto.command;

import java.util.UUID;

public record DeliverOrderCommand(
        UUID orderId,
        UUID sellerId,
        String deliveryNote,
        String deliverableUrl
) {
}
