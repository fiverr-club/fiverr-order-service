package club.fiverr.order.application.mapper;

import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.domain.model.Order;

public final class OrderResultMapper {

    private OrderResultMapper() {
    }

    public static OrderResult toResult(Order order) {
        return new OrderResult(
                order.getId(),
                order.getGigId(),
                order.getBuyerId(),
                order.getSellerId(),
                order.getPackageTier(),
                order.getAmount(),
                order.getRequirements(),
                order.getMaxRevisions(),
                order.getUsedRevisions(),
                order.getStatus(),
                order.getDeliveryNote(),
                order.getDeliverableUrl(),
                order.getLastRevisionReason(),
                order.getEscrowReference(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
