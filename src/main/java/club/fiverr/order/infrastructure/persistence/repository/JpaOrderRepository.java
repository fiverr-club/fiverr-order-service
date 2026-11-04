package club.fiverr.order.infrastructure.persistence.repository;

import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.repository.OrderRepository;
import club.fiverr.order.infrastructure.persistence.entity.OrderEntity;
import club.fiverr.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaOrderRepository implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderPersistenceMapper mapper;

    public JpaOrderRepository(OrderJpaRepository jpaRepository, OrderPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity saved = jpaRepository.save(mapper.toEntity(order));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Order> findByBuyerId(UUID buyerId) {
        return jpaRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Order> findBySellerId(UUID sellerId) {
        return jpaRepository.findBySellerIdOrderByCreatedAtDesc(sellerId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
