package club.fiverr.order.application.port.in;

import club.fiverr.order.application.dto.command.DeliverOrderCommand;
import club.fiverr.order.application.dto.result.OrderResult;

public interface DeliverOrderUseCase {

    OrderResult execute(DeliverOrderCommand command);
}
