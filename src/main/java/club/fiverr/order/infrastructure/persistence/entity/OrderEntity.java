package club.fiverr.order.infrastructure.persistence.entity;

import club.fiverr.order.domain.model.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "gig_id", nullable = false)
    private UUID gigId;

    @Column(name = "buyer_id", nullable = false)
    private UUID buyerId;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "package_tier", nullable = false, length = 30)
    private String packageTier;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "requirements", nullable = false, length = 2000)
    private String requirements;

    @Column(name = "max_revisions", nullable = false)
    private int maxRevisions;

    @Column(name = "used_revisions", nullable = false)
    private int usedRevisions;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status;

    @Column(name = "delivery_note", length = 2000)
    private String deliveryNote;

    @Column(name = "deliverable_url", length = 500)
    private String deliverableUrl;

    @Column(name = "last_revision_reason", length = 1000)
    private String lastRevisionReason;

    @Column(name = "escrow_reference", length = 120)
    private String escrowReference;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public OrderEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGigId() {
        return gigId;
    }

    public void setGigId(UUID gigId) {
        this.gigId = gigId;
    }

    public UUID getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(UUID buyerId) {
        this.buyerId = buyerId;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public void setSellerId(UUID sellerId) {
        this.sellerId = sellerId;
    }

    public String getPackageTier() {
        return packageTier;
    }

    public void setPackageTier(String packageTier) {
        this.packageTier = packageTier;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getMaxRevisions() {
        return maxRevisions;
    }

    public void setMaxRevisions(int maxRevisions) {
        this.maxRevisions = maxRevisions;
    }

    public int getUsedRevisions() {
        return usedRevisions;
    }

    public void setUsedRevisions(int usedRevisions) {
        this.usedRevisions = usedRevisions;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getDeliverableUrl() {
        return deliverableUrl;
    }

    public void setDeliverableUrl(String deliverableUrl) {
        this.deliverableUrl = deliverableUrl;
    }

    public String getLastRevisionReason() {
        return lastRevisionReason;
    }

    public void setLastRevisionReason(String lastRevisionReason) {
        this.lastRevisionReason = lastRevisionReason;
    }

    public String getEscrowReference() {
        return escrowReference;
    }

    public void setEscrowReference(String escrowReference) {
        this.escrowReference = escrowReference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
