package org.cetide.hibiscus.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Set;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${hibiscus.jwt.secret}")
    private String secret;
    
    @Value("${hibiscus.jwt.expiration}")
    private Long expiration;

    @Value("${hibiscus.jwt.header}")
    private String tokenHeader;

    @Value("${hibiscus.jwt.token-start}")
    private String tokenPrefix;

    public JwtUtils(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(tokenHeader);
        if (header != null && header.startsWith(tokenPrefix + " ")) {
            return header.substring(tokenPrefix.length() + 1);
        }
        return null;
    }

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !isTokenBlacklisted(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenBlacklisted(String token) {
        String username = String.valueOf(getUserIdFromToken(token));
        return redisTemplate.opsForSet().isMember("jwt:blacklist:"+username, token);
    }

    public String refreshToken(String oldToken) {
        if (validateToken(oldToken)) {
            Date now = new Date();
            Date expiryDate = getExpirationDateFromToken(oldToken);
            if (expiryDate.getTime() - now.getTime() < 300000) { // 5分钟内过期
                return generateToken(String.valueOf(getUserIdFromToken(oldToken)));
            }
        }
        return null;
    }
    
    public void invalidateToken(String token) {
        String username = String.valueOf(getUserIdFromToken(token));
        redisTemplate.opsForSet().add("jwt:blacklist:"+username, token);
    }

    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨3点执行
    public void cleanExpiredBlacklist() {
        Set<String> keys = redisTemplate.keys("jwt:blacklist:*");
        if (keys != null) {
            keys.forEach(key -> {
                Long count = redisTemplate.opsForZSet().removeRangeByScore(key, 0,
                    System.currentTimeMillis() - expiration * 1000);
                log.info("清理过期黑名单令牌: {} 个", count);
            });
        }
    }

    private Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }
}