package com.demo.admindemo.domain.dto.guest;

import com.demo.admindemo.domain.enums.GuestStatus;
import lombok.Data;

@Data
public class GuestRegisterDTO {
    private String centerName;
    private String businessNumber;
    private String name;
    private String phoneNumber;
    private String email;
    private String notes;
    private String preferredUserId;
    private String password;
    private GuestStatus status = GuestStatus.PENDING;
}
