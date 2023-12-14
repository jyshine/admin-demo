package com.demo.admindemo.domain.dto.guest;

import com.demo.admindemo.domain.enums.GuestStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GuestViewDTO {
    private String centerName;
    private String businessNumber;
    private String name;
    private String phoneNumber;
    private String email;
    private String notes;
    private String preferredUserId;
    private GuestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
