package com.demo.admindemo.domain.mapper;


import com.demo.admindemo.domain.dto.guest.GuestRegisterDTO;
import com.demo.admindemo.domain.dto.guest.GuestViewDTO;
import com.demo.admindemo.domain.entity.Guest;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    Guest guestRegisterDTOToGuestEntity(GuestRegisterDTO guestRegisterDTO);
    GuestViewDTO guestEntityToGuestViewDTO(Guest guest);
    List<GuestViewDTO> guestEntityToGuestViewDTO(List<Guest> guests);

}
