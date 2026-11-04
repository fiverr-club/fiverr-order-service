package club.fiverr.order.application.port.in;

import club.fiverr.order.application.dto.command.CompleteOrderCommand;
import club.fiverr.order.application.dto.result.OrderResult;

public interface CompleteOrderUseCase {

    OrderResult execute(CompleteOrderCommand command);
}
