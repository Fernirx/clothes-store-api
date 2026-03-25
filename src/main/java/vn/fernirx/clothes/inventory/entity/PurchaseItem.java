package vn.fernirx.clothes.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fernirx.clothes.catalog.entity.ProductVariant;
import vn.fernirx.clothes.common.entity.BaseEntity;
import vn.fernirx.clothes.inventory.enums.QualityStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase_items")
public class PurchaseItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @Column(name = "quantity_ordered", nullable = false)
    private int quantityOrdered;

    @Column(name = "quantity_received")
    private int quantityReceived = 0;

    @Column(name = "unit_cost", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitCost;

    @Column(name = "line_total", precision = 15, scale = 2)
    private BigDecimal lineTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "quality_status", nullable = false)
    private QualityStatus qualityStatus = QualityStatus.PENDING;

    @Column(name = "defective_qty")
    private int defectiveQty = 0;

    @Column(name = "defect_reason", columnDefinition = "TEXT")
    private String defectReason;

    @Column(name = "is_received")
    private Boolean isReceived = false;

    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
