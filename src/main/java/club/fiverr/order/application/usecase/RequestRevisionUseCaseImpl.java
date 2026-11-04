package club.fiverr.order.application.usecase;

import club.fiverr.order.application.dto.command.RequestRevisionCommand;
import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.application.mapper.OrderResultMapper;
import club.fiverr.order.application.port.in.RequestRevisionUseCase;
import club.fiverr.order.application.port.out.OrderEventPublisherPort;
import club.fiverr.order.domain.exception.InvalidOrderStateException;
import club.fiverr.order.domain.exception.OrderNotFoundException;
import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestRevisionUseCaseImpl implements RequestRevisionUseCase {

    private final OrderRepository orderRepository;
    private final OrderEventPublisherPort orderEventPublisherPort;

    public RequestRevisionUseCaseImpl(
            OrderRepository orderRepository,
            OrderEventPublisherPort orderEventPublisherPort
    ) {
        this.orderRepository = orderRepository;
        this.orderEventPublisherPort = orderEventPublisherPort;
    }

    @Override
    @Transactional
    public OrderResult execute(RequestRevisionCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));

        if (!order.getBuyerId().equals(command.buyerId())) {
            throw new InvalidOrderStateException("Only the buyer of this order can request a revision");
        }

        order.requestRevision(command.reason());
        Order saved = orderRepository.save(order);
        orderEventPublisherPort.publishRevisionRequested(saved);

        return OrderResultMapper.toResult(saved);
    }
}
