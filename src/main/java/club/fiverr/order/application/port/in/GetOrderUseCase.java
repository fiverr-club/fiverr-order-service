package club.fiverr.order.application.port.in;

import club.fiverr.order.application.dto.result.OrderResult;

import java.util.List;
import java.util.UUID;

public interface GetOrderUseCase {

    OrderResult getById(UUID orderId);

    List<OrderResult> getAll();
}
