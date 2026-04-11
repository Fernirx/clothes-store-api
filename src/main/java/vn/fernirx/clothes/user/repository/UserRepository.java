package vn.fernirx.clothes.user.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.fernirx.clothes.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE u.id = :id", nativeQuery = true)
    Optional<User> findByIdIncludeDeleted(@Param("id") Long id);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users u WHERE u.email = :email)", nativeQuery = true)
    Integer existsByEmailIncludeDeleted(@Param("email") String email);

    @Query(value = "SELECT * FROM users u WHERE u.email = :email", nativeQuery = true)
    Optional<User> findByEmailIncludeDeleted(@Param("email") String email);

    @Modifying
    @Query(value = "DELETE FROM users WHERE id = :id", nativeQuery = true)
    void hardDeleteById(@Param("id") Long id);
}