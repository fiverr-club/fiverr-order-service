package club.fiverr.order.application.usecase;

import club.fiverr.order.OrderTestFixtures;
import club.fiverr.order.application.dto.command.CompleteOrderCommand;
import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.application.port.out.EscrowPaymentPort;
import club.fiverr.order.application.port.out.OrderEventPublisherPort;
import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.model.OrderStatus;
import club.fiverr.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompleteOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EscrowPaymentPort escrowPaymentPort;

    @Mock
    private OrderEventPublisherPort orderEventPublisherPort;

    @InjectMocks
    private CompleteOrderUseCaseImpl completeOrderUseCase;

    @Test
    void shouldCompleteOrderAndReleaseEscrowFunds() {
        Order delivered = OrderTestFixtures.createDeliveredOrder();
        when(orderRepository.findById(OrderTestFixtures.DEFAULT_ORDER_ID)).thenReturn(Optional.of(delivered));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        CompleteOrderCommand command = new CompleteOrderCommand(
                OrderTestFixtures.DEFAULT_ORDER_ID,
                OrderTestFixtures.DEFAULT_BUYER_ID
        );

        OrderResult result = completeOrderUseCase.execute(command);

        assertEquals(OrderStatus.COMPLETED, result.status());
        verify(escrowPaymentPort).releaseEscrowFunds(OrderTestFixtures.DEFAULT_ORDER_ID, "ESCROW-AAAAAAAA");
        verify(orderRepository).save(any(Order.class));
        verify(orderEventPublisherPort).publishOrderCompleted(any(Order.class));
    }
}
