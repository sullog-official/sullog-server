package sullog.backend.member.service;

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
import sullog.backend.member.dto.MemberDto;
import sullog.backend.member.entity.Token;

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

    public TokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token generateToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
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
            log.error("잘만료된 jwt 토큰입니다", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 jwt 토큰입니다", e);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 jwt 토큰입니다", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        MemberDto memberDto = MemberDto.builder()
                .email(email)
                //TODO: DB에서 데이터 읽어와 나머지 필드 셋팅
                .build();

        return new UsernamePasswordAuthenticationToken(memberDto, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

}
