package vn.fernirx.clothes.user.init;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "application.security.admin")
public class AdminProperties {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
