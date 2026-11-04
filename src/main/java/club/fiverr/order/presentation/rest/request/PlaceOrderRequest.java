package club.fiverr.order.presentation.rest.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PlaceOrderRequest(
        @NotNull UUID gigId,
        @NotNull UUID buyerId,
        @NotNull UUID sellerId,
        @NotBlank String packageTier,
        @NotNull @DecimalMin("5.00") BigDecimal amount,
        @NotBlank String requirements,
        @Min(0) int maxRevisions
) {
}
