package club.fiverr.order;

import club.fiverr.order.application.dto.command.PlaceOrderCommand;
import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class OrderTestFixtures {

    public static final UUID DEFAULT_ORDER_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    public static final UUID DEFAULT_GIG_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID DEFAULT_BUYER_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID DEFAULT_SELLER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private OrderTestFixtures() {
    }

    public static Order createInProgressOrder() {
        return Order.restore(
                DEFAULT_ORDER_ID,
                DEFAULT_GIG_ID,
                DEFAULT_BUYER_ID,
                DEFAULT_SELLER_ID,
                "STANDARD",
                new BigDecimal("150.00"),
                "Please build a Spring Boot 3.3.5 DDD microservice with PostgreSQL",
                2,
                0,
                OrderStatus.IN_PROGRESS,
                null,
                null,
                null,
                "ESCROW-AAAAAAAA",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-01T00:00:00Z")
        );
    }

    public static Order createDeliveredOrder() {
        Order order = createInProgressOrder();
        order.deliver("Completed the DDD microservice with full unit test coverage", "https://cdn.fiverr.club/deliveries/order-1.zip");
        return order;
    }

    public static PlaceOrderCommand createValidPlaceOrderCommand() {
        return new PlaceOrderCommand(
                DEFAULT_GIG_ID,
                DEFAULT_BUYER_ID,
                DEFAULT_SELLER_ID,
                "STANDARD",
                new BigDecimal("150.00"),
                "Please build a Spring Boot 3.3.5 DDD microservice with PostgreSQL",
                2
        );
    }
}
