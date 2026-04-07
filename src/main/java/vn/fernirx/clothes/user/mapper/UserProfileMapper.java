package vn.fernirx.clothes.user.mapper;

import org.mapstruct.*;
import vn.fernirx.clothes.user.dto.response.UserProfileResponse;
import vn.fernirx.clothes.user.entity.UserProfile;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {
    @Mapping(target = "email", source = "user.email")
    UserProfileResponse toDto(UserProfile userProfile);
}