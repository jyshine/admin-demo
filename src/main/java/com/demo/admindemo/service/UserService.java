package com.demo.admindemo.service;


import com.demo.admindemo.domain.dto.user.UserDTO;
import com.demo.admindemo.domain.mapper.UserMapper;
import com.demo.admindemo.domain.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AdminRepository adminRepository;
    private final UserMapper userMapper;

    public UserDTO getUserDetailInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();
        return userMapper.adminToUserDTO(adminRepository.findByUser_UserName(userName));
    }
}
