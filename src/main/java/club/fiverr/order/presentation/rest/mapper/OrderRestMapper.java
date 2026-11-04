package club.fiverr.order.presentation.rest.mapper;

import club.fiverr.order.application.dto.command.CompleteOrderCommand;
import club.fiverr.order.application.dto.command.DeliverOrderCommand;
import club.fiverr.order.application.dto.command.PlaceOrderCommand;
import club.fiverr.order.application.dto.command.RequestRevisionCommand;
import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.presentation.rest.request.CompleteOrderRequest;
import club.fiverr.order.presentation.rest.request.DeliverOrderRequest;
import club.fiverr.order.presentation.rest.request.PlaceOrderRequest;
import club.fiverr.order.presentation.rest.request.RequestRevisionRequest;
import club.fiverr.order.presentation.rest.response.OrderResponse;

import java.util.UUID;

public final class OrderRestMapper {

    private OrderRestMapper() {
    }

    public static PlaceOrderCommand toCommand(PlaceOrderRequest request) {
        return new PlaceOrderCommand(
                request.gigId(),
                request.buyerId(),
                request.sellerId(),
                request.packageTier(),
                request.amount(),
                request.requirements(),
                request.maxRevisions()
        );
    }

    public static DeliverOrderCommand toCommand(UUID orderId, DeliverOrderRequest request) {
        return new DeliverOrderCommand(
                orderId,
                request.sellerId(),
                request.deliveryNote(),
                request.deliverableUrl()
        );
    }

    public static RequestRevisionCommand toCommand(UUID orderId, RequestRevisionRequest request) {
        return new RequestRevisionCommand(
                orderId,
                request.buyerId(),
                request.reason()
        );
    }

    public static CompleteOrderCommand toCommand(UUID orderId, CompleteOrderRequest request) {
        return new CompleteOrderCommand(
                orderId,
                request.buyerId()
        );
    }

    public static OrderResponse toResponse(OrderResult result) {
        return new OrderResponse(
                result.id(),
                result.gigId(),
                result.buyerId(),
                result.sellerId(),
                result.packageTier(),
                result.amount(),
                result.requirements(),
                result.maxRevisions(),
                result.usedRevisions(),
                result.status(),
                result.deliveryNote(),
                result.deliverableUrl(),
                result.lastRevisionReason(),
                result.escrowReference(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
