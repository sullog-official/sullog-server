package sullog.backend.member.config.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sullog.backend.member.service.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JwtAuthFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    //jwt 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if (isRootContext(httpServletRequest)) return;
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (tokenService.validateToken(token)) { // JWT 토큰이 유효한 경우에만, USER객체 셋팅
            Authentication authentication = tokenService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse); // 다음 Filter를 실행(마지막 필터라면 필터 실행 후 리소스를 반환)
    }

    /**
     * AWS 에서 서버 health check를 하게 되는데 default가 /로 체크를 해 해당 메소드가 없다면 401 에러가 나게됨
     * 추후 health check 전용 컨텍스트를 만들던지 현행 유지
     */
    private static boolean isRootContext(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRequestURI().equals("/");
    }
}