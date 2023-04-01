package sullog.backend.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.member.entity.Token;
import sullog.backend.member.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<Void> refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        int memberId = tokenService.getMemberId(token);
        Token newToken = tokenService.generateToken(memberId, "USER");

        response.addHeader(HttpHeaders.AUTHORIZATION, newToken.getAccessToken());
        response.addHeader("Refresh", newToken.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
