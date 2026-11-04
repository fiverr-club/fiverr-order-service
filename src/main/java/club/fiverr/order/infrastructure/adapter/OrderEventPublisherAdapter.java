package club.fiverr.order.infrastructure.adapter;

import club.fiverr.order.application.port.out.OrderEventPublisherPort;
import club.fiverr.order.domain.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisherAdapter implements OrderEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisherAdapter.class);

    @Override
    public void publishOrderPlaced(Order order) {
        log.info("Published OrderPlacedEvent: orderId={}, status={}", order.getId(), order.getStatus());
    }

    @Override
    public void publishOrderDelivered(Order order) {
        log.info("Published OrderDeliveredEvent: orderId={}, deliverableUrl={}",
                order.getId(), order.getDeliverableUrl());
    }

    @Override
    public void publishRevisionRequested(Order order) {
        log.info("Published OrderRevisionRequestedEvent: orderId={}, usedRevisions={}/{}",
                order.getId(), order.getUsedRevisions(), order.getMaxRevisions());
    }

    @Override
    public void publishOrderCompleted(Order order) {
        log.info("Published OrderCompletedEvent: orderId={}, escrowReference={}",
                order.getId(), order.getEscrowReference());
    }
}
