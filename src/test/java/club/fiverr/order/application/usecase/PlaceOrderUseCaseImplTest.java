package club.fiverr.order.application.usecase;

import club.fiverr.order.OrderTestFixtures;
import club.fiverr.order.application.dto.command.PlaceOrderCommand;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EscrowPaymentPort escrowPaymentPort;

    @Mock
    private OrderEventPublisherPort orderEventPublisherPort;

    @InjectMocks
    private PlaceOrderUseCaseImpl placeOrderUseCase;

    @Test
    void shouldSuccessfullyPlaceOrderHoldEscrowAndTransitionToInProgress() {
        PlaceOrderCommand command = OrderTestFixtures.createValidPlaceOrderCommand();
        when(escrowPaymentPort.holdEscrowFunds(any(), any(), any(), any())).thenReturn("ESCROW-99887766");
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResult result = placeOrderUseCase.execute(command);

        assertNotNull(result);
        assertEquals(OrderStatus.IN_PROGRESS, result.status());
        assertEquals("ESCROW-99887766", result.escrowReference());
        verify(orderRepository).save(any(Order.class));
        verify(orderEventPublisherPort).publishOrderPlaced(any(Order.class));
    }
}
