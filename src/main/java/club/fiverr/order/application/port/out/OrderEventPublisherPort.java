package club.fiverr.order.application.port.out;

import club.fiverr.order.domain.model.Order;

public interface OrderEventPublisherPort {

    void publishOrderPlaced(Order order);

    void publishOrderDelivered(Order order);

    void publishRevisionRequested(Order order);

    void publishOrderCompleted(Order order);
}
