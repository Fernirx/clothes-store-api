package vn.fernirx.clothes.user.mapper;

import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vn.fernirx.clothes.common.enums.UserRole;
import vn.fernirx.clothes.security.CustomUserDetails;
import vn.fernirx.clothes.user.dto.response.UserResponse;
import vn.fernirx.clothes.user.entity.User;

import java.util.Collection;
import java.util.Collections;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toDto(User user);

    @Mapping(target = "authorities", expression = "java(mapAuthorities(user.getRole()))")
    CustomUserDetails toCustomUserDetails(User user);

    default Collection<GrantedAuthority> mapAuthorities(UserRole userRoles) {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + userRoles.name()));
    }
}