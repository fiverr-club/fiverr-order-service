package club.fiverr.order.application.usecase;

import club.fiverr.order.application.dto.command.DeliverOrderCommand;
import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.application.mapper.OrderResultMapper;
import club.fiverr.order.application.port.in.DeliverOrderUseCase;
import club.fiverr.order.application.port.out.OrderEventPublisherPort;
import club.fiverr.order.domain.exception.InvalidOrderStateException;
import club.fiverr.order.domain.exception.OrderNotFoundException;
import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliverOrderUseCaseImpl implements DeliverOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderEventPublisherPort orderEventPublisherPort;

    public DeliverOrderUseCaseImpl(
            OrderRepository orderRepository,
            OrderEventPublisherPort orderEventPublisherPort
    ) {
        this.orderRepository = orderRepository;
        this.orderEventPublisherPort = orderEventPublisherPort;
    }

    @Override
    @Transactional
    public OrderResult execute(DeliverOrderCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));

        if (!order.getSellerId().equals(command.sellerId())) {
            throw new InvalidOrderStateException("Only the assigned seller can deliver this order");
        }

        order.deliver(command.deliveryNote(), command.deliverableUrl());
        Order saved = orderRepository.save(order);
        orderEventPublisherPort.publishOrderDelivered(saved);

        return OrderResultMapper.toResult(saved);
    }
}
