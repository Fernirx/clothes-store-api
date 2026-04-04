package vn.fernirx.clothes.user.repository;

import org.springframework.data.jpa.domain.Specification;
import vn.fernirx.clothes.user.dto.request.UserFilterRequest;
import vn.fernirx.clothes.user.entity.User;

public class UserSpecification {
    public static Specification<User> hasRole(String role) {
        return (root, query, cb) -> role == null ? null
                : cb.equal(root.get("role"), role);
    }

    public static Specification<User> hasProvider(String provider) {
        return (root, query, cb) ->  provider == null ? null
                : cb.equal(root.get("provider"), provider);
    }

    public static Specification<User> hasActive(Boolean active) {
        return (root, query, cb) -> active == null ? null
                : cb.equal(root.get("active"), active);
    }

    public static Specification<User> build(UserFilterRequest filter) {
        return Specification
                .where(hasRole(filter.role()))
                .and(hasProvider(filter.provider()))
                .and(hasActive(filter.active()));
    }
}
