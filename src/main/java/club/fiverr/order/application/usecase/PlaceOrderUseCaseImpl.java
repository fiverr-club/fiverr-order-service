package club.fiverr.order.application.usecase;

import club.fiverr.order.application.dto.command.PlaceOrderCommand;
import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.application.mapper.OrderResultMapper;
import club.fiverr.order.application.port.in.PlaceOrderUseCase;
import club.fiverr.order.application.port.out.EscrowPaymentPort;
import club.fiverr.order.application.port.out.OrderEventPublisherPort;
import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaceOrderUseCaseImpl implements PlaceOrderUseCase {

    private final OrderRepository orderRepository;
    private final EscrowPaymentPort escrowPaymentPort;
    private final OrderEventPublisherPort orderEventPublisherPort;

    public PlaceOrderUseCaseImpl(
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
    public OrderResult execute(PlaceOrderCommand command) {
        Order order = Order.createNew(
                command.gigId(),
                command.buyerId(),
                command.sellerId(),
                command.packageTier(),
                command.amount(),
                command.requirements(),
                command.maxRevisions()
        );

        String escrowRef = escrowPaymentPort.holdEscrowFunds(
                order.getId(),
                order.getBuyerId(),
                order.getSellerId(),
                order.getAmount()
        );
        order.startProgress(escrowRef);

        Order saved = orderRepository.save(order);
        orderEventPublisherPort.publishOrderPlaced(saved);

        return OrderResultMapper.toResult(saved);
    }
}
