package com.demo.admindemo.domain.dto.guest;


import com.demo.admindemo.domain.enums.GuestStatus;

public record GuestModifyDTO(
        Long guestId,
        GuestStatus status
) {
}
