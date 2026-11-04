package club.fiverr.order.application.dto.command;

import java.util.UUID;

public record CompleteOrderCommand(
        UUID orderId,
        UUID buyerId
) {
}
