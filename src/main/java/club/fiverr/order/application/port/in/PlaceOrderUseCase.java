package club.fiverr.order.application.port.in;

import club.fiverr.order.application.dto.command.PlaceOrderCommand;
import club.fiverr.order.application.dto.result.OrderResult;

public interface PlaceOrderUseCase {

    OrderResult execute(PlaceOrderCommand command);
}
