package club.fiverr.order.presentation.rest.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CompleteOrderRequest(
        @NotNull UUID buyerId
) {
}
