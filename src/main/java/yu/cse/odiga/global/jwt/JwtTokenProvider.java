package yu.cse.odiga.global.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import yu.cse.odiga.auth.application.CustomUserDetailsService;

@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final long accessTokenExpireSeconds;
    private final Key key;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-expire-seconds}") long accessTokenExpireSeconds,
                            CustomUserDetailsService customUserDetailsService) {
        this.accessTokenExpireSeconds = accessTokenExpireSeconds;
        this.customUserDetailsService = customUserDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenDto createToken(String email, String nickname, String role) {
        long now = (new Date()).getTime();
        Date expireTime = new Date(now + this.accessTokenExpireSeconds);

        String accessToken = Jwts.builder()
                .setSubject(email)
                .claim("auth", role)
                .claim("nickname", nickname)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expireTime)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
//                .claim("nickname", nickname)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expireTime)
                .compact();

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String email = claims.getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {

            logger.info("만료된 JWT 토큰입니다.");

        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");

        } catch (IllegalArgumentException e) {

            logger.info("JWT 토큰이 잘못되었습니다.");

        }
        return false;
    }
}


