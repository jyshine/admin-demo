package com.demo.admindemo.controller;

import static com.demo.admindemo.config.web.Path.REFRESHTOKEN;
import static com.demo.admindemo.config.web.Path.SIGN_IN;
import static com.demo.admindemo.config.web.Path.SIGN_OUT;

import com.demo.admindemo.config.web.RestResponse;
import com.demo.admindemo.domain.dto.auth.SigninDTO;
import com.demo.admindemo.domain.dto.auth.UserTokenDTO;
import com.demo.admindemo.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Value("${cookie.path}")
    private String cookiePath;
    @Value("${cookie.access-token.name}")
    private String cookieAccessTokenName;
    @Value("${cookie.refresh-token.name}")
    private String cokkieRefreshTokenName;
    @Value("${cookie.max-age.value}")
    private int cookieMaxageValue;
    @Value("${cookie.max-age.logout}")
    private int cookieMaxageLogout;

    private final AuthService authService;

    @PostMapping(SIGN_IN)
    public RestResponse<UserTokenDTO> signIn(@RequestBody SigninDTO signinDTO, HttpServletResponse response) {
        UserTokenDTO userTokenDTO = authService.authenticateUser(signinDTO);
        setResponseCookies(response, userTokenDTO.getAccessToken(),userTokenDTO.getRefreshToken(), cookieMaxageValue, cookiePath);
        return new RestResponse<>(userTokenDTO);
    }

    @PostMapping(SIGN_OUT)
    public ResponseEntity<String> signOut(HttpServletResponse response) {
        setResponseCookies(response, null,null, cookieMaxageLogout, cookiePath);
        return ResponseEntity.ok().body("You've been signed out!");
    }

    @PostMapping(REFRESHTOKEN)
    public RestResponse<UserTokenDTO> refreshToken(@Valid @RequestBody UserTokenDTO userTokenDTO, HttpServletResponse response) {
        UserTokenDTO resultUserTokenDTO = authService.refreshToken(userTokenDTO);
        setResponseCookies(response, resultUserTokenDTO.getAccessToken(),resultUserTokenDTO.getRefreshToken(), cookieMaxageValue, cookiePath);
        return new RestResponse<>(resultUserTokenDTO);
    }

    private void setResponseCookies(HttpServletResponse response, String accessToken, String refreshToken,  int cookieMaxAge,String path) {
        Cookie accessTokenCookie = createCookie(cookieAccessTokenName, refreshToken, cookieMaxAge, path);
        Cookie refreshTokenCookie = createCookie(cokkieRefreshTokenName, accessToken, cookieMaxAge, path);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    private static Cookie createCookie(String tokenType, String token, int maxAge, String path) {
        Cookie tokenCookie = new Cookie(tokenType, token);
        tokenCookie.setMaxAge(maxAge); // expires in 7 days
        tokenCookie.setSecure(false);
        tokenCookie.setHttpOnly(false);
        tokenCookie.setPath(path);
        return tokenCookie;
    }
}
