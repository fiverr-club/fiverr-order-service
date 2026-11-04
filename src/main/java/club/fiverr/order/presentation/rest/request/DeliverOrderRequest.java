package club.fiverr.order.presentation.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeliverOrderRequest(
        @NotNull UUID sellerId,
        @NotBlank String deliveryNote,
        @NotBlank String deliverableUrl
) {
}
