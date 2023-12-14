package com.demo.admindemo.domain.mapper;

import com.demo.admindemo.domain.dto.auth.UserTokenDTO;
import com.demo.admindemo.domain.entity.UserToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTokenMapper {
    UserTokenDTO userToUserTokenDTO(UserToken userToken);
}
