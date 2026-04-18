package vn.fernirx.clothes.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import vn.fernirx.clothes.common.entity.BaseEntity;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "brands", indexes = {@Index(name = "idx_brands_active",
        columnList = "is_active")}, uniqueConstraints = {
        @UniqueConstraint(name = "name_UNIQUE",
                columnNames = {"name"}),
        @UniqueConstraint(name = "slug_UNIQUE",
                columnNames = {"slug"})})
public class Brand extends BaseEntity {
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

    @Size(max = 500)
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Size(max = 255)
    @Column(name = "logo_public_id")
    private String logoPublicId;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<Product> products = new LinkedHashSet<>();
}