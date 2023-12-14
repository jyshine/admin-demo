package com.demo.admindemo.service;

import com.demo.admindemo.domain.dto.auth.UserTokenDTO;
import com.demo.admindemo.domain.entity.UserToken;
import com.demo.admindemo.domain.repository.UserRepository;
import com.demo.admindemo.domain.repository.UserTokenRepository;
import com.demo.admindemo.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserToken createUserToken(String userName, String accessToken, String refreshToken, Date atExpiryDate,
                                     Date rtExpiryDate) {
        Optional<UserToken> byUserName = userTokenRepository.findByUserName(userName);

        if (byUserName.isPresent()) {
            // 이미 해당 userName에 대한 UserToken이 존재하면 업데이트 수행
            UserToken existingToken = byUserName.get();
            existingToken.setAccessToken(accessToken);
            existingToken.setRefreshToken(refreshToken);
            existingToken.setAtExpiryDate(atExpiryDate.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
            existingToken.setRtExpiryDate(rtExpiryDate.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
            return userTokenRepository.save(existingToken);
        } else {
            // 해당 userName에 대한 UserToken이 없으면 새로 생성하여 저장
            UserToken newToken = UserToken.builder()
                    .userName(userName)
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .atExpiryDate(atExpiryDate.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                    .rtExpiryDate(rtExpiryDate.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                    .build();

            return userTokenRepository.save(newToken);
        }
    }

    public UserToken getUserTokenByUserName(String userName) {
        return userTokenRepository.findByUserName(userName).orElseThrow(
                () -> UnauthorizedException
                        .withSystemMessage("Not Found Refresh Token By UserName")
                        .withUserMessage("해당 사용자의 Refresh 토큰을 찾을 수 없습니다.").build()
        );
    }

//    public int removeRefreshTokenByUserId(Long userId) {
//        Optional<User> findUser = userRepository.findById(userId);
//        User user = findUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return userTokenRepository.deleteByUserId(userId);
//    }
    public void verifyToken(UserTokenDTO userTokenDTO, UserToken findUserToken) {
        if (isVerifyTokenValue(userTokenDTO, findUserToken)) {
            throw UnauthorizedException
                    .withSystemMessage("Token information does not match.")
                    .withUserMessageKey("토큰 정보가 일치하지 않습니다.")
                    .build();
        }

        if (isVerifyExpiration(findUserToken)) {
            throw UnauthorizedException
                    .withSystemMessage("Refresh token was expired.")
                    .withUserMessageKey("만료된 토큰입니다.")
                    .build();
        }
    }

    public boolean isVerifyTokenValue(UserTokenDTO userTokenDTO, UserToken findUserToken) {
        return !(userTokenDTO.getAccessToken().equals(findUserToken.getAccessToken()) &&
                userTokenDTO.getRefreshToken().equals(findUserToken.getRefreshToken()));
    }
    public boolean isVerifyExpiration(UserToken refreshToken) {
        return refreshToken.getRtExpiryDate().isBefore(LocalDateTime.now());
    }

    @Transactional
    public void removeUserToken(UserToken findUserToken) {
        userTokenRepository.delete(findUserToken);
    }
}
