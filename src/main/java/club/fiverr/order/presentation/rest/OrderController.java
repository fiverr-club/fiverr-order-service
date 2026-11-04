package club.fiverr.order.presentation.rest;

import club.fiverr.order.application.dto.result.OrderResult;
import club.fiverr.order.application.port.in.CompleteOrderUseCase;
import club.fiverr.order.application.port.in.DeliverOrderUseCase;
import club.fiverr.order.application.port.in.PlaceOrderUseCase;
import club.fiverr.order.application.port.in.RequestRevisionUseCase;
import club.fiverr.order.presentation.rest.mapper.OrderRestMapper;
import club.fiverr.order.presentation.rest.request.CompleteOrderRequest;
import club.fiverr.order.presentation.rest.request.DeliverOrderRequest;
import club.fiverr.order.presentation.rest.request.PlaceOrderRequest;
import club.fiverr.order.presentation.rest.request.RequestRevisionRequest;
import club.fiverr.order.presentation.rest.response.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final DeliverOrderUseCase deliverOrderUseCase;
    private final RequestRevisionUseCase requestRevisionUseCase;
    private final CompleteOrderUseCase completeOrderUseCase;

    public OrderController(
            PlaceOrderUseCase placeOrderUseCase,
            DeliverOrderUseCase deliverOrderUseCase,
            RequestRevisionUseCase requestRevisionUseCase,
            CompleteOrderUseCase completeOrderUseCase
    ) {
        this.placeOrderUseCase = placeOrderUseCase;
        this.deliverOrderUseCase = deliverOrderUseCase;
        this.requestRevisionUseCase = requestRevisionUseCase;
        this.completeOrderUseCase = completeOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        OrderResult result = placeOrderUseCase.execute(OrderRestMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderRestMapper.toResponse(result));
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody DeliverOrderRequest request
    ) {
        OrderResult result = deliverOrderUseCase.execute(OrderRestMapper.toCommand(orderId, request));
        return ResponseEntity.ok(OrderRestMapper.toResponse(result));
    }

    @PostMapping("/{orderId}/revisions")
    public ResponseEntity<OrderResponse> requestRevision(
            @PathVariable UUID orderId,
            @Valid @RequestBody RequestRevisionRequest request
    ) {
        OrderResult result = requestRevisionUseCase.execute(OrderRestMapper.toCommand(orderId, request));
        return ResponseEntity.ok(OrderRestMapper.toResponse(result));
    }

    @PostMapping("/{orderId}/complete")
    public ResponseEntity<OrderResponse> completeOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody CompleteOrderRequest request
    ) {
        OrderResult result = completeOrderUseCase.execute(OrderRestMapper.toCommand(orderId, request));
        return ResponseEntity.ok(OrderRestMapper.toResponse(result));
    }
}
