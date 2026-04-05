package vn.fernirx.clothes.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.user.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
}