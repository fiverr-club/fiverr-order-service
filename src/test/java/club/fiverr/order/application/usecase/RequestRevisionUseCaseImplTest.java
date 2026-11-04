package club.fiverr.order.application.usecase;

import club.fiverr.order.OrderTestFixtures;
import club.fiverr.order.application.dto.command.RequestRevisionCommand;
import club.fiverr.order.application.dto.result.OrderResult;
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
class RequestRevisionUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisherPort orderEventPublisherPort;

    @InjectMocks
    private RequestRevisionUseCaseImpl requestRevisionUseCase;

    @Test
    void shouldSuccessfullyRequestRevisionOnDeliveredOrder() {
        Order delivered = OrderTestFixtures.createDeliveredOrder();
        when(orderRepository.findById(OrderTestFixtures.DEFAULT_ORDER_ID)).thenReturn(Optional.of(delivered));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        RequestRevisionCommand command = new RequestRevisionCommand(
                OrderTestFixtures.DEFAULT_ORDER_ID,
                OrderTestFixtures.DEFAULT_BUYER_ID,
                "Please add OpenAPI documentation"
        );

        OrderResult result = requestRevisionUseCase.execute(command);

        assertEquals(OrderStatus.REVISION_REQUESTED, result.status());
        assertEquals(1, result.usedRevisions());
        assertEquals("Please add OpenAPI documentation", result.lastRevisionReason());
        verify(orderRepository).save(any(Order.class));
        verify(orderEventPublisherPort).publishRevisionRequested(any(Order.class));
    }
}
