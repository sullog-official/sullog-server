package sullog.backend.common.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sullog.backend.auth.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MemberIdInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public MemberIdInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (null == token) { // permitAll로 처리되는 요청은 jwtToken값을 실어보내지 않으므로, 방어로직 추가
            return true;
        }
        Integer memberId = tokenService.getMemberId(token);
        request.setAttribute("memberId", memberId);
        return true;
    }
}
