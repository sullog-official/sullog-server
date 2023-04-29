package sullog.backend.common.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sullog.backend.auth.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MemberIdInterceptor extends HandlerInterceptorAdapter {

    private final TokenService tokenService;

    public MemberIdInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        Integer memberId = tokenService.getMemberId(token);
        request.setAttribute("memberId", memberId);
        return true;
    }
}
