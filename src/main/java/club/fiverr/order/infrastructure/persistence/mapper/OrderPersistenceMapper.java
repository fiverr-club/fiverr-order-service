package club.fiverr.order.infrastructure.persistence.mapper;

import club.fiverr.order.domain.model.Order;
import club.fiverr.order.infrastructure.persistence.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceMapper {

    public OrderEntity toEntity(Order domain) {
        OrderEntity entity = new OrderEntity();
        entity.setId(domain.getId());
        entity.setGigId(domain.getGigId());
        entity.setBuyerId(domain.getBuyerId());
        entity.setSellerId(domain.getSellerId());
        entity.setPackageTier(domain.getPackageTier());
        entity.setAmount(domain.getAmount());
        entity.setRequirements(domain.getRequirements());
        entity.setMaxRevisions(domain.getMaxRevisions());
        entity.setUsedRevisions(domain.getUsedRevisions());
        entity.setStatus(domain.getStatus());
        entity.setDeliveryNote(domain.getDeliveryNote());
        entity.setDeliverableUrl(domain.getDeliverableUrl());
        entity.setLastRevisionReason(domain.getLastRevisionReason());
        entity.setEscrowReference(domain.getEscrowReference());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        return Order.restore(
                entity.getId(),
                entity.getGigId(),
                entity.getBuyerId(),
                entity.getSellerId(),
                entity.getPackageTier(),
                entity.getAmount(),
                entity.getRequirements(),
                entity.getMaxRevisions(),
                entity.getUsedRevisions(),
                entity.getStatus(),
                entity.getDeliveryNote(),
                entity.getDeliverableUrl(),
                entity.getLastRevisionReason(),
                entity.getEscrowReference(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
