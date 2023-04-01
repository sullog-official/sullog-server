package sullog.backend.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import sullog.backend.member.entity.Member;
import sullog.backend.member.entity.Token;
import sullog.backend.member.service.MemberService;

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
public class TokenService implements InitializingBean {

    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private Key key;

    private final MemberService memberService;

    public TokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds,
            MemberService memberService) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
        this.memberService = memberService;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token generateToken(int memberId, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        claims.put("role", role);

        Instant now = Instant.now();
        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                        .setExpiration(new Date(now.toEpochMilli() + accessTokenValidityInMilliseconds))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                        .setExpiration(new Date(now.toEpochMilli() + refreshTokenValidityInMilliseconds))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact());
    }

    public boolean validateToken(String token) {
        if(token == null) {
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 jwt 서명을 가진 토큰입니다", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 jwt 토큰입니다", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 jwt 토큰입니다", e);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 jwt 토큰입니다", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        int memberId = getMemberId(token);
        Member findMember = memberService.findMemberById(memberId);
        return new UsernamePasswordAuthenticationToken(findMember, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public int getMemberId(String token) {
        String subject = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        return Integer.parseInt(subject);
    }

}
