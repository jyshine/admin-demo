package com.demo.admindemo.domain.mapper;

import com.demo.admindemo.domain.dto.user.UserDTO;
import com.demo.admindemo.domain.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO adminToUserDTO(Admin admin);
}
