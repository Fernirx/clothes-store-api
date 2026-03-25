package vn.fernirx.clothes.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fernirx.clothes.common.entity.BaseEntity;
import vn.fernirx.clothes.inventory.enums.AdjustmentStatus;
import vn.fernirx.clothes.inventory.enums.AdjustmentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stock_adjustments")
public class StockAdjustment extends BaseEntity {

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AdjustmentType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdjustmentStatus status = AdjustmentStatus.DRAFT;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @OneToMany(mappedBy = "adjustment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockAdjustmentItem> items = new ArrayList<>();
}
