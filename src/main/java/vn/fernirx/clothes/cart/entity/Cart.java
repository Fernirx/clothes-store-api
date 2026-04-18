package vn.fernirx.clothes.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.fernirx.clothes.common.entity.BaseEntity;
import vn.fernirx.clothes.user.entity.User;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "carts", uniqueConstraints = {
        @UniqueConstraint(name = "user_cart_user_UNIQUE",
                columnNames = {"user_id"}),
        @UniqueConstraint(name = "user_cart_guest_UNIQUE",
                columnNames = {"guest_token"})})
public class Cart extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "guest_token", referencedColumnName = "guest_token")
    private GuestSession guestSession;

    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItems = new LinkedHashSet<>();
}