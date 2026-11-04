package club.fiverr.order.application.usecase;

import club.fiverr.order.application.dto.command.CompleteOrderCommand;
import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.application.mapper.OrderResultMapper;
import club.fiverr.order.application.port.in.CompleteOrderUseCase;
import club.fiverr.order.application.port.out.EscrowPaymentPort;
import club.fiverr.order.application.port.out.OrderEventPublisherPort;
import club.fiverr.order.domain.exception.InvalidOrderStateException;
import club.fiverr.order.domain.exception.OrderNotFoundException;
import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompleteOrderUseCaseImpl implements CompleteOrderUseCase {

    private final OrderRepository orderRepository;
    private final EscrowPaymentPort escrowPaymentPort;
    private final OrderEventPublisherPort orderEventPublisherPort;

    public CompleteOrderUseCaseImpl(
            OrderRepository orderRepository,
            EscrowPaymentPort escrowPaymentPort,
            OrderEventPublisherPort orderEventPublisherPort
    ) {
        this.orderRepository = orderRepository;
        this.escrowPaymentPort = escrowPaymentPort;
        this.orderEventPublisherPort = orderEventPublisherPort;
    }

    @Override
    @Transactional
    public OrderResult execute(CompleteOrderCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));

        if (!order.getBuyerId().equals(command.buyerId())) {
            throw new InvalidOrderStateException("Only the buyer of this order can mark it completed");
        }

        order.complete();
        escrowPaymentPort.releaseEscrowFunds(order.getId(), order.getEscrowReference());

        Order saved = orderRepository.save(order);
        orderEventPublisherPort.publishOrderCompleted(saved);

        return OrderResultMapper.toResult(saved);
    }
}
