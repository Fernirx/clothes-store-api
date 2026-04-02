package vn.fernirx.clothes.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}