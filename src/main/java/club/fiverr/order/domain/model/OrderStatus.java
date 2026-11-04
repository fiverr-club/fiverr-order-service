package club.fiverr.order.domain.model;

public enum OrderStatus {
    CREATED,
    IN_PROGRESS,
    DELIVERED,
    REVISION_REQUESTED,
    COMPLETED,
    CANCELLED,
    DISPUTED
}
