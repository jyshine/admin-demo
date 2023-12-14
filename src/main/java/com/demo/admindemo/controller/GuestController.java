package com.demo.admindemo.controller;

import static com.demo.admindemo.config.web.Path.GUEST;

import com.demo.admindemo.config.web.PageDTO;
import com.demo.admindemo.config.web.RestResponse;
import com.demo.admindemo.domain.dto.guest.GuestModifyDTO;
import com.demo.admindemo.domain.dto.guest.GuestRegisterDTO;
import com.demo.admindemo.domain.dto.guest.GuestViewDTO;
import com.demo.admindemo.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @PostMapping(GUEST)
    public RestResponse<GuestViewDTO> createGuest(@RequestBody GuestRegisterDTO guestRegisterDTO) {
        return new RestResponse<>(guestService.createGuest(guestRegisterDTO));
    }

    @GetMapping(GUEST)
    public RestResponse<PageDTO<GuestViewDTO>> getGuests(Pageable pageable){
        return new RestResponse<>(guestService.getAllGuest(pageable));
    }

    @PutMapping(GUEST)
    public RestResponse<String> modifyGuest(@RequestBody GuestModifyDTO guestModifyDTO) {
        return new RestResponse<>(guestService.modifyGuest(guestModifyDTO));
    }
}
