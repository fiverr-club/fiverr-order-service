package club.fiverr.order.domain.model;

import club.fiverr.order.OrderTestFixtures;
import club.fiverr.order.domain.exception.InvalidOrderStateException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void shouldTransitionThroughFullOrderStateMachine() {
        Order order = Order.createNew(
                OrderTestFixtures.DEFAULT_GIG_ID,
                OrderTestFixtures.DEFAULT_BUYER_ID,
                OrderTestFixtures.DEFAULT_SELLER_ID,
                "PREMIUM",
                new BigDecimal("350.00"),
                "Enterprise microservice architecture with DDD",
                1
        );

        assertEquals(OrderStatus.CREATED, order.getStatus());

        order.startProgress("ESCROW-12345678");
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());

        order.deliver("Initial delivery ready", "https://cdn.fiverr.club/deliveries/v1.zip");
        assertEquals(OrderStatus.DELIVERED, order.getStatus());

        order.requestRevision("Please add Kubernetes readiness probes");
        assertEquals(OrderStatus.REVISION_REQUESTED, order.getStatus());
        assertEquals(1, order.getUsedRevisions());

        order.deliver("Updated delivery with readiness probes", "https://cdn.fiverr.club/deliveries/v2.zip");
        assertEquals(OrderStatus.DELIVERED, order.getStatus());

        order.complete();
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    void shouldRejectRevisionWhenMaxRevisionsExhausted() {
        Order order = Order.createNew(
                OrderTestFixtures.DEFAULT_GIG_ID,
                OrderTestFixtures.DEFAULT_BUYER_ID,
                OrderTestFixtures.DEFAULT_SELLER_ID,
                "BASIC",
                new BigDecimal("50.00"),
                "Basic microservice setup",
                1
        );
        order.startProgress("ESCROW-BASIC");
        order.deliver("First delivery", "https://cdn.fiverr.club/deliveries/1.zip");
        order.requestRevision("First revision");
        order.deliver("Second delivery", "https://cdn.fiverr.club/deliveries/2.zip");

        assertThrows(InvalidOrderStateException.class, () -> order.requestRevision("Second revision exceeding limit"));
    }

    @Test
    void shouldSupportCancellationAndDisputeTransitions() {
        Order order = OrderTestFixtures.createInProgressOrder();
        order.openDispute();
        assertEquals(OrderStatus.DISPUTED, order.getStatus());

        Order cancellable = OrderTestFixtures.createInProgressOrder();
        cancellable.cancel();
        assertEquals(OrderStatus.CANCELLED, cancellable.getStatus());
    }
}
