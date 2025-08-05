package ir.maktabsharif.homeserviceprovidersystem.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import ir.maktabsharif.homeserviceprovidersystem.exception.SecurityCustomException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private int jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(MyUserDetails myUserDetails) {
        return Jwts.builder()
                .setSubject(myUserDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            throw new SecurityCustomException("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new SecurityCustomException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new SecurityCustomException("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new SecurityCustomException("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new SecurityCustomException("JWT claims string is empty: " + e.getMessage());
        }
    }
}
