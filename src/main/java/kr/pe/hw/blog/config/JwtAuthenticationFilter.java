package kr.pe.hw.blog.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //1.Request Header에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) servletRequest);

        //2.validateToken으로 토큰 유효성 검사
        if(token != null && tokenProvider.validateToken(token)) {
            //토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    //Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // 임시 방편 나중에 수정해봐
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("Authorization")) bearerToken = cookie.getValue();
        } // 여기까지

//        log.info("-----------------------------------------------");
//        log.info("requestURL : {}", request.getRequestURL());
//        log.info("requestURI : {}", request.getRequestURI());
//        log.info("requestMethod : {}", request.getMethod());
//        log.info("requestHeaders : {}", request.getHeaderNames());
//        Cookie cookies[] = request.getCookies();
//        for(int i = 0; i < cookies.length; i++) {
//            String name = cookies[i].getName();
//            String value = cookies[i].getValue();
//            logger.info(name + "=" + value);
//        }
//        log.info("-----------------------------------------------");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
