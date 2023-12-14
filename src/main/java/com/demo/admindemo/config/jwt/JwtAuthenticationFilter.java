package com.demo.admindemo.config.jwt;

import com.demo.admindemo.config.web.RestResponse;
import com.demo.admindemo.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {

            String token = resolveToken(request);

            if (!StringUtils.hasText(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String userName = jwtService.extractUserName(token);

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException e) {
            RestResponse<Object> restResponse = new RestResponse<>();
            restResponse.setSystemMessage(e.getSystemMessage());
            restResponse.setUserMessage(e.getUserMessage());
            sendErrorResponse(response, restResponse);
        } catch (MalformedJwtException e) {
            RestResponse<Object> restResponse = new RestResponse<>();
            restResponse.setSystemMessage("MalformedJwtException");
            restResponse.setUserMessage("손상된 토큰입니다.");
            sendErrorResponse(response, restResponse);
        } catch (ExpiredJwtException e) {
            RestResponse<Object> restResponse = new RestResponse<>();
            restResponse.setSystemMessage("ExpiredJwtException");
            restResponse.setUserMessage("만료된 Access 토큰입니다");
            sendErrorResponse(response, restResponse);
        } catch (UnsupportedJwtException e) {
            RestResponse<Object> restResponse = new RestResponse<>();
            restResponse.setSystemMessage("UnsupportedJwtException");
            restResponse.setUserMessage("지원하지 않는 토큰입니다.");
        } catch (SignatureException e) {
            RestResponse<Object> restResponse = new RestResponse<>();
            restResponse.setSystemMessage("SignatureException");
            restResponse.setUserMessage("시그니처 검증에 실패한 토큰입니다");
        }
    }

    /**
     * 헤더 token 추출
     * @param request HttpServletRequest
     * @return 헤더 토큰 추출 값
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }

    /**
     * jwt 예외처리 응답
     */
    private void sendErrorResponse(HttpServletResponse response, RestResponse restResponse) throws IOException {
        restResponse.setCode("401");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(restResponse));
    }
}
