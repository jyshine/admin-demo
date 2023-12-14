package com.demo.admindemo.service;


import com.demo.admindemo.config.web.PageDTO;
import com.demo.admindemo.domain.dto.guest.GuestModifyDTO;
import com.demo.admindemo.domain.dto.guest.GuestRegisterDTO;
import com.demo.admindemo.domain.dto.guest.GuestViewDTO;
import com.demo.admindemo.domain.entity.Admin;
import com.demo.admindemo.domain.entity.Guest;
import com.demo.admindemo.domain.entity.User;
import com.demo.admindemo.domain.enums.UserType;
import com.demo.admindemo.domain.mapper.GuestMapper;
import com.demo.admindemo.domain.repository.AdminRepository;
import com.demo.admindemo.domain.repository.GuestRepository;
import com.demo.admindemo.domain.repository.UserRepository;
import com.demo.admindemo.exception.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GuestMapper guestMapper;

    @Transactional
    public GuestViewDTO createGuest(GuestRegisterDTO guestRegisterDTO) {
        Guest guest = guestMapper.guestRegisterDTOToGuestEntity(guestRegisterDTO);
        guest.setPassword(passwordEncoder.encode(guestRegisterDTO.getPassword()));
        return guestMapper.guestEntityToGuestViewDTO(guestRepository.save(guest));
    }

    public PageDTO<GuestViewDTO> getAllGuest(Pageable pageable){
        Page<Guest> all = guestRepository.findAll(pageable);
        List<Guest> content = all.getContent();

        PageDTO<GuestViewDTO> pageDTO = new PageDTO<>();
        pageDTO.setPage(all.getPageable());
        pageDTO.setTotalCount((int) all.getTotalElements());
        pageDTO.setList(guestMapper.guestEntityToGuestViewDTO(content));
        pageDTO.setFirst(all.isFirst());
        pageDTO.setLast(all.isLast());

        return pageDTO;
    }

    @Transactional
    public String modifyGuest(GuestModifyDTO guestModifyDTO) {
        Optional<Guest> findGuest = guestRepository.findById(guestModifyDTO.guestId());

        Guest guest = findGuest.orElseThrow(
                () -> NotFoundException
                        .withUserMessageKey("guest.notfound")
                        .withSystemMessage("not found guest").build());

        User build = User.builder()
                .userName(guest.getPreferredUserId())
                .userType(UserType.ADMIN)
                .phoneNumber(guest.getPhoneNumber())
                .password(guest.getPassword()).build();
        User save = userRepository.save(build);

        Admin admin = new Admin(guest.getName(), guest.getEmail(), "123123123", save);
        Admin saved = adminRepository.save(admin);

        guest.changeGuestStatus(guestModifyDTO.status());

        return saved.getUser().getUsername();
    }
}
