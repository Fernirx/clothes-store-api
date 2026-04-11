package vn.fernirx.clothes.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vn.fernirx.clothes.common.enums.UserRole;
import vn.fernirx.clothes.security.CustomUserDetails;
import vn.fernirx.clothes.user.dto.request.CreateUserRequest;
import vn.fernirx.clothes.user.dto.response.UserResponse;
import vn.fernirx.clothes.user.entity.User;

import java.util.Collection;
import java.util.Collections;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toDto(User user);

    User toUserFromCreateRequest(CreateUserRequest createUserRequest);

    @Mapping(target = "authorities", expression = "java(mapAuthorities(user.getRole()))")
    @Mapping(target = "password", source = "passwordHash")
    CustomUserDetails toCustomUserDetails(User user);

    default Collection<GrantedAuthority> mapAuthorities(UserRole userRoles) {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + userRoles.name()));
    }
}