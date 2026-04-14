package vn.fernirx.clothes.catalog.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "brands", indexes = {@jakarta.persistence.Index(name = "idx_brands_active",
columnList = "is_active")}, uniqueConstraints = {
@jakarta.persistence.UniqueConstraint(name = "name_UNIQUE",
columnNames = {"name"}),
@jakarta.persistence.UniqueConstraint(name = "slug_UNIQUE",
columnNames = {"slug"})})
public class Brand extends vn.fernirx.clothes.common.entity.BaseEntity {
@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "name", nullable = false, length = 100)
private java.lang.String name;

@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "slug", nullable = false, length = 100)
private java.lang.String slug;

@jakarta.persistence.Lob
@jakarta.persistence.Column(name = "description")
private java.lang.String description;

@jakarta.validation.constraints.Size(max = 500)
@jakarta.persistence.Column(name = "logo_url", length = 500)
private java.lang.String logoUrl;

@jakarta.validation.constraints.Size(max = 255)
@jakarta.persistence.Column(name = "logo_public_id")
private java.lang.String logoPublicId;

@jakarta.validation.constraints.NotNull
@org.hibernate.annotations.ColumnDefault("1")
@jakarta.persistence.Column(name = "is_active", nullable = false)
private java.lang.Boolean isActive;

@jakarta.persistence.OneToMany
@jakarta.persistence.JoinColumn(name = "brand_id")
private java.util.Set<vn.fernirx.clothes.catalog.entity.Product> products = new java.util.LinkedHashSet<>();



}