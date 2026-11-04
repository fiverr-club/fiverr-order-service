package club.fiverr.order.infrastructure.config;

import club.fiverr.order.domain.model.Order;
import club.fiverr.order.domain.model.OrderStatus;
import club.fiverr.order.domain.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Configuration
public class OrderDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OrderDataInitializer.class);

    private final OrderRepository orderRepository;

    public OrderDataInitializer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        if (!orderRepository.findAll().isEmpty()) {
            return;
        }

        Instant now = Instant.now();

        Order order1 = Order.restore(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "PREMIUM",
                new BigDecimal("995.00"),
                "Architect 8 Java 21 Spring Boot DDD microservices + API Gateway + Kubernetes Helm charts",
                10,
                0,
                OrderStatus.IN_PROGRESS,
                null,
                null,
                null,
                "ESCROW-44444444",
                now.minusSeconds(86400 * 3),
                now.minusSeconds(86400 * 2)
        );

        Order order2 = Order.restore(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "STANDARD",
                new BigDecimal("390.00"),
                "Build Next.js 15 App Router FIVERREX marketplace frontend with 3-tier checkout and order dashboard",
                4,
                1,
                OrderStatus.DELIVERED,
                "Delivered full Next.js 15 React 19 storefront with live API integration and Playwright tests.",
                "https://github.com/fiverr-club/fiverr-web-frontend",
                "Added interactive Swagger UI banner and mobile responsive drawer.",
                "ESCROW-AAAAAAAA",
                now.minusSeconds(86400 * 5),
                now.minusSeconds(86400)
        );

        Order order3 = Order.restore(
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "PREMIUM",
                new BigDecimal("595.00"),
                "Design complete FIVERREX Figma design system, green/slate tokens, and component library",
                6,
                0,
                OrderStatus.COMPLETED,
                "Delivered 120+ Figma components, design tokens, and developer handoff specs.",
                "https://figma.com/@fiverrex/design-system-v1",
                null,
                "ESCROW-BBBBBBBB",
                now.minusSeconds(86400 * 7),
                now
        );

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        log.info("Seeded 3 sample FIVERREX orders (IN_PROGRESS, DELIVERED, COMPLETED) into Order database");
    }
}
