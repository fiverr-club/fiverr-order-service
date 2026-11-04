package club.fiverr.order.application.usecase;

import club.fiverr.order.OrderTestFixtures;
import club.fiverr.order.application.dto.command.DeliverOrderCommand;
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
class DeliverOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisherPort orderEventPublisherPort;

    @InjectMocks
    private DeliverOrderUseCaseImpl deliverOrderUseCase;

    @Test
    void shouldSuccessfullyDeliverOrder() {
        Order order = OrderTestFixtures.createInProgressOrder();
        when(orderRepository.findById(OrderTestFixtures.DEFAULT_ORDER_ID)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        DeliverOrderCommand command = new DeliverOrderCommand(
                OrderTestFixtures.DEFAULT_ORDER_ID,
                OrderTestFixtures.DEFAULT_SELLER_ID,
                "Here is your completed microservice archive",
                "https://cdn.fiverr.club/deliveries/final.zip"
        );

        OrderResult result = deliverOrderUseCase.execute(command);

        assertEquals(OrderStatus.DELIVERED, result.status());
        assertEquals("https://cdn.fiverr.club/deliveries/final.zip", result.deliverableUrl());
        verify(orderRepository).save(any(Order.class));
        verify(orderEventPublisherPort).publishOrderDelivered(any(Order.class));
    }
}
