package club.fiverr.order.application.usecase;

import club.fiverr.order.OrderTestFixtures;
import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.domain.exception.OrderNotFoundException;
import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderUseCaseImpl getOrderUseCase;

    @Test
    void shouldReturnOrderByIdWhenFound() {
        Order order = OrderTestFixtures.createInProgressOrder();
        when(orderRepository.findById(OrderTestFixtures.DEFAULT_ORDER_ID)).thenReturn(Optional.of(order));

        OrderResult result = getOrderUseCase.getById(OrderTestFixtures.DEFAULT_ORDER_ID);

        assertNotNull(result);
        assertEquals(OrderTestFixtures.DEFAULT_ORDER_ID, result.id());
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenMissing() {
        UUID missingId = UUID.randomUUID();
        when(orderRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> getOrderUseCase.getById(missingId));
    }

    @Test
    void shouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(
                OrderTestFixtures.createInProgressOrder(),
                OrderTestFixtures.createDeliveredOrder()
        ));

        List<OrderResult> results = getOrderUseCase.getAll();

        assertEquals(2, results.size());
    }
}
