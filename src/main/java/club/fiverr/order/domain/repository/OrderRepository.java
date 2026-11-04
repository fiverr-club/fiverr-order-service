package club.fiverr.order.domain.repository;

import club.fiverr.order.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAll();

    List<Order> findByBuyerId(UUID buyerId);

    List<Order> findBySellerId(UUID sellerId);
}
