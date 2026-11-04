package club.fiverr.order.application.usecase;

import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.application.mapper.OrderResultMapper;
import club.fiverr.order.application.port.in.GetOrderUseCase;
import club.fiverr.order.domain.exception.OrderNotFoundException;
import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GetOrderUseCaseImpl implements GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCaseImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResult getById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return OrderResultMapper.toResult(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResult> getAll() {
        return orderRepository.findAll().stream()
                .map(OrderResultMapper::toResult)
                .toList();
    }
}
