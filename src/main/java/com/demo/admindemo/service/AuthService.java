package com.demo.admindemo.service;

import com.demo.admindemo.config.jwt.JwtService;
import com.demo.admindemo.domain.dto.auth.SigninDTO;
import com.demo.admindemo.domain.dto.auth.UserTokenDTO;
import com.demo.admindemo.domain.entity.UserToken;
import com.demo.admindemo.domain.mapper.UserTokenMapper;
import com.demo.admindemo.domain.repository.UserRepository;
import com.demo.admindemo.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.transaction.Transactional;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserTokenService userTokenService;
    private final UserTokenMapper userTokenMapper;


    @Transactional
    public UserTokenDTO authenticateUser(SigninDTO signinDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinDTO.userName(), signinDTO.password()));

        return createNewToken(authentication);
    }

    @Transactional
    public UserTokenDTO refreshToken(UserTokenDTO userTokenDTO) {
        Authentication authentication;

        try {
            String requestRefreshToken = userTokenDTO.getRefreshToken();
            String userName = jwtService.extractUserName(requestRefreshToken);
            UserToken findUserToken = userTokenService.getUserTokenByUserName(userName);
            userTokenService.verifyToken(userTokenDTO, findUserToken);
            authentication = jwtService.getAuthentication(userTokenDTO.getRefreshToken());

        } catch (SignatureException e){
            throw UnauthorizedException
                    .withSystemMessage(e.getMessage())
                    .withUserMessage("시그니처 검증에 실패한 토큰입니다")
                    .withCode("401")
                    .build();
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException
                    .withSystemMessage(e.getMessage())
                    .withUserMessage("만료된 Refresh 토큰입니다")
                    .withCode("401")
                    .build();
        }

        return createNewToken(authentication);
    }

    private UserTokenDTO createNewToken(Authentication authentication) {
        long now = (new Date()).getTime();

//        Date atExpiryDate = new Date(now+60000);
//        Date rtExpiryDate = new Date(now+120000);
        Date atExpiryDate = new Date(now + (1000L * 60 * 60 * 24));
        Date rtExpiryDate = new Date(now + (1000L * 60 * 60 * 24 * 7));

        String accessToken = jwtService.generateToken(authentication, atExpiryDate);
        String refreshToken = jwtService.generateToken(authentication, rtExpiryDate);

        UserToken userToken = userTokenService.createUserToken(authentication.getName(), accessToken, refreshToken, atExpiryDate, rtExpiryDate);
        return userTokenMapper.userToUserTokenDTO(userToken);
    }

}
