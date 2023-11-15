package io.ylab.walletservice.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.ylab.walletservice.core.domain.PlayerRole;
import io.ylab.walletservice.core.dto.player.PlayerResponse;
import io.ylab.walletservice.util.PropertiesUtil;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@UtilityClass
public class JwtProvider {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateTokenForPlayer(PlayerResponse player) {
        return Jwts.builder()
                .claim("id", player.id())
                .claim("username", player.username())
                .claim("role", player.role())
                .issuer("http://localhost:8080/")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .signWith(Keys.hmacShaKeyFor(getSecret()))
                .compact();
    }

    public static Long getPlayerIdFromToken(String token) {
        return getPayload(token).get("id", Long.class);
    }

    public static String getPlayerUsernameFromToken(String token) {
        return getPayload(token).get("username", String.class);
    }

    public static PlayerRole getPlayerRoleFromToken(String token) {
        return getPayload(token).get("role", PlayerRole.class);
    }

    public static boolean verifyToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSecret())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException | MalformedJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static Claims getPayload(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(getSecret()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateSecret() {
        byte[] randomBytes = new byte[64];
        RANDOM.nextBytes(randomBytes);
        String updatedSecret = Base64.getEncoder().encodeToString(randomBytes);
        PropertiesUtil.setPropertyValue("jwt.token.secret", updatedSecret);
    }

    private static byte[] getSecret() {
        return Base64.getDecoder().decode(PropertiesUtil.get("jwt.token.secret"));
    }
}
