package sullog.backend.common.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sullog.backend.auth.service.TokenService;
import sullog.backend.common.error.ErrorCode;
import sullog.backend.common.error.exception.BusinessException;
import sullog.backend.member.entity.Member;
import sullog.backend.member.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MemberIdInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final MemberService memberService;

    public MemberIdInterceptor(TokenService tokenService, MemberService memberService) {
        this.tokenService = tokenService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (null == token) { // permitAll로 처리되는 요청은 jwtToken값을 실어보내지 않으므로, 방어로직 추가
            return true;
        }

        Integer memberId = tokenService.getMemberId(token);
        Member findMember = memberService.findMemberById(memberId); // 토큰에 담긴 memberId로 member가 조회되지 않으면 예외 발생
        if (null == findMember) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

        request.setAttribute("memberId", memberId);
        return true;
    }
}
