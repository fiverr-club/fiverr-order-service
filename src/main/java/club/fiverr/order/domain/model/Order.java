package club.fiverr.order.domain.model;

import club.fiverr.order.domain.exception.InvalidOrderStateException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Order {

    private final UUID id;
    private final UUID gigId;
    private final UUID buyerId;
    private final UUID sellerId;
    private final String packageTier;
    private final BigDecimal amount;
    private final String requirements;
    private final int maxRevisions;
    private int usedRevisions;
    private OrderStatus status;
    private String deliveryNote;
    private String deliverableUrl;
    private String lastRevisionReason;
    private String escrowReference;
    private final Instant createdAt;
    private Instant updatedAt;

    private Order(
            UUID id,
            UUID gigId,
            UUID buyerId,
            UUID sellerId,
            String packageTier,
            BigDecimal amount,
            String requirements,
            int maxRevisions,
            int usedRevisions,
            OrderStatus status,
            String deliveryNote,
            String deliverableUrl,
            String lastRevisionReason,
            String escrowReference,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.gigId = gigId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.packageTier = packageTier;
        this.amount = amount;
        this.requirements = requirements;
        this.maxRevisions = maxRevisions;
        this.usedRevisions = usedRevisions;
        this.status = status;
        this.deliveryNote = deliveryNote;
        this.deliverableUrl = deliverableUrl;
        this.lastRevisionReason = lastRevisionReason;
        this.escrowReference = escrowReference;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order createNew(
            UUID gigId,
            UUID buyerId,
            UUID sellerId,
            String packageTier,
            BigDecimal amount,
            String requirements,
            int maxRevisions
    ) {
        if (gigId == null || buyerId == null || sellerId == null) {
            throw new InvalidOrderStateException("Gig ID, Buyer ID, and Seller ID must not be null");
        }
        if (buyerId.equals(sellerId)) {
            throw new InvalidOrderStateException("Buyer cannot purchase their own gig");
        }
        if (packageTier == null || packageTier.isBlank()) {
            throw new InvalidOrderStateException("Package tier must not be blank");
        }
        if (amount == null || amount.compareTo(BigDecimal.valueOf(5)) < 0) {
            throw new InvalidOrderStateException("Order amount must be at least $5.00");
        }
        if (requirements == null || requirements.isBlank()) {
            throw new InvalidOrderStateException("Order requirements must not be blank");
        }
        if (maxRevisions < 0) {
            throw new InvalidOrderStateException("Max revisions cannot be negative");
        }

        Instant now = Instant.now();
        return new Order(
                UUID.randomUUID(),
                gigId,
                buyerId,
                sellerId,
                packageTier.trim().toUpperCase(),
                amount,
                requirements.trim(),
                maxRevisions,
                0,
                OrderStatus.CREATED,
                null,
                null,
                null,
                null,
                now,
                now
        );
    }

    public static Order restore(
            UUID id,
            UUID gigId,
            UUID buyerId,
            UUID sellerId,
            String packageTier,
            BigDecimal amount,
            String requirements,
            int maxRevisions,
            int usedRevisions,
            OrderStatus status,
            String deliveryNote,
            String deliverableUrl,
            String lastRevisionReason,
            String escrowReference,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Order(
                id,
                gigId,
                buyerId,
                sellerId,
                packageTier,
                amount,
                requirements,
                maxRevisions,
                usedRevisions,
                status,
                deliveryNote,
                deliverableUrl,
                lastRevisionReason,
                escrowReference,
                createdAt,
                updatedAt
        );
    }

    public void startProgress(String escrowReference) {
        if (this.status != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(
                    "Order can only transition to IN_PROGRESS from CREATED state, current state: " + this.status
            );
        }
        if (escrowReference == null || escrowReference.isBlank()) {
            throw new InvalidOrderStateException("Escrow reference is required to start order progress");
        }
        this.escrowReference = escrowReference.trim();
        this.status = OrderStatus.IN_PROGRESS;
        this.updatedAt = Instant.now();
    }

    public void deliver(String deliveryNote, String deliverableUrl) {
        if (this.status != OrderStatus.IN_PROGRESS && this.status != OrderStatus.REVISION_REQUESTED) {
            throw new InvalidOrderStateException(
                    "Order can only be delivered from IN_PROGRESS or REVISION_REQUESTED state, current state: " + this.status
            );
        }
        if (deliveryNote == null || deliveryNote.isBlank()) {
            throw new InvalidOrderStateException("Delivery note must not be blank");
        }
        if (deliverableUrl == null || deliverableUrl.isBlank()) {
            throw new InvalidOrderStateException("Deliverable URL must not be blank");
        }
        this.deliveryNote = deliveryNote.trim();
        this.deliverableUrl = deliverableUrl.trim();
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = Instant.now();
    }

    public void requestRevision(String reason) {
        if (this.status != OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException(
                    "Revision can only be requested when order is in DELIVERED state, current state: " + this.status
            );
        }
        if (this.usedRevisions >= this.maxRevisions) {
            throw new InvalidOrderStateException(
                    "Maximum revisions (" + this.maxRevisions + ") already exhausted for this order"
            );
        }
        if (reason == null || reason.isBlank()) {
            throw new InvalidOrderStateException("Revision reason must not be blank");
        }
        this.usedRevisions++;
        this.lastRevisionReason = reason.trim();
        this.status = OrderStatus.REVISION_REQUESTED;
        this.updatedAt = Instant.now();
    }

    public void complete() {
        if (this.status != OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException(
                    "Order can only be completed from DELIVERED state, current state: " + this.status
            );
        }
        this.status = OrderStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        if (this.status == OrderStatus.COMPLETED || this.status == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Cannot cancel an order in state: " + this.status);
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    public void openDispute() {
        if (this.status != OrderStatus.IN_PROGRESS
                && this.status != OrderStatus.DELIVERED
                && this.status != OrderStatus.REVISION_REQUESTED) {
            throw new InvalidOrderStateException("Cannot open dispute for order in state: " + this.status);
        }
        this.status = OrderStatus.DISPUTED;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getGigId() {
        return gigId;
    }

    public UUID getBuyerId() {
        return buyerId;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public String getPackageTier() {
        return packageTier;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getRequirements() {
        return requirements;
    }

    public int getMaxRevisions() {
        return maxRevisions;
    }

    public int getUsedRevisions() {
        return usedRevisions;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public String getDeliverableUrl() {
        return deliverableUrl;
    }

    public String getLastRevisionReason() {
        return lastRevisionReason;
    }

    public String getEscrowReference() {
        return escrowReference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
