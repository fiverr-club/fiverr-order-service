package club.fiverr.order.application.dto.command;

import java.util.UUID;

public record RequestRevisionCommand(
        UUID orderId,
        UUID buyerId,
        String reason
) {
}
