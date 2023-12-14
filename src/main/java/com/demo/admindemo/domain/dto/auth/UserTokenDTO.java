package com.demo.admindemo.domain.dto.auth;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserTokenDTO {
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String accessToken;
    private LocalDateTime atExpiryDate;
    private LocalDateTime rtExpiryDate;
}
