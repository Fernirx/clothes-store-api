package vn.fernirx.clothes.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.fernirx.clothes.common.entity.BaseEntity;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories", indexes = {@Index(name = "idx_categories_parent_active",
        columnList = "parent_id, is_active")}, uniqueConstraints = {
        @UniqueConstraint(name = "name_UNIQUE",
                columnNames = {"name"}),
        @UniqueConstraint(name = "slug_UNIQUE",
                columnNames = {"slug"})})
public class Category extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100)
    @NotNull
    @Column(name = "slug", nullable = false, length = 100)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Category> children = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<ProductCategory> productCategories = new LinkedHashSet<>();
}