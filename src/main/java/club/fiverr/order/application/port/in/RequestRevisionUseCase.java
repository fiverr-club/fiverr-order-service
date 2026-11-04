package club.fiverr.order.application.port.in;

import club.fiverr.order.application.dto.command.RequestRevisionCommand;
import club.fiverr.order.application.dto.result.OrderResult;

public interface RequestRevisionUseCase {

    OrderResult execute(RequestRevisionCommand command);
}
