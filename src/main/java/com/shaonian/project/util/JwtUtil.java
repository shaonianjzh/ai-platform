package com.shaonian.project.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.config.JwtConfig;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.model.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 少年
 */
@Component
@Slf4j
public class JwtUtil {

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 创建JWT
     *
     * @param id          用户id
     * @param subject     用户账号
     * @param role       用户角色
     * @return JWT
     */
    public String createJWT( Long id, String subject, String role) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder().setId(id.toString())
                .setSubject(subject).setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getKey())
                .claim("roles", role);

        // 设置过期时间
        Long ttl = jwtConfig.getExpireTime();
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));
        }
        String jwt = builder.compact();
        // 将生成的JWT保存至Redis
        stringRedisTemplate.opsForValue().set("jwt:" + subject, jwt, ttl, TimeUnit.MILLISECONDS);
        return jwt;
    }

    /**
     * 创建JWT
     *
     * @param user 用户认证信息
     * @return JWT
     */
    public String createJWT(User user) {
        return createJWT(user.getId(),user.getUserAccount(),user.getUserRole());
    }

    /**
     * 解析JWT
     *
     * @param jwt JWT
     * @return {@link Claims}
     */
    public Claims parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getKey()).parseClaimsJws(jwt).getBody();

            String username = claims.getSubject();
            String redisKey = "jwt:" + username;

            // 校验redis中的JWT是否存在
            Long expire = stringRedisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
            if (Objects.isNull(expire) || expire <= 0) {
                throw new BusinessException(ErrorCode.TOKEN_PARSE_ERROR);
            }

            // 校验redis中的JWT是否与当前的一致，不一致则代表用户已注销/用户在不同设备登录，均代表JWT已过期
            String redisToken = stringRedisTemplate.opsForValue().get(redisKey);
            if (!StrUtil.equals(jwt, redisToken)) {
                throw new BusinessException(ErrorCode.TOKEN_PARSE_ERROR);
            }
            return claims;
        } catch (ExpiredJwtException e) {
            log.error("Tokne 解析失败");
            throw new BusinessException(ErrorCode.TOKEN_PARSE_ERROR);
        }
    }

    /**
     * 设置JWT过期
     *
     * @param request 请求
     */
    public void invalidateJWT(HttpServletRequest request) {
        String jwt = getJwtFromRequest(request);
        String username = getUsernameFromJWT(jwt);
        // 从redis中清除JWT
        stringRedisTemplate.delete("jwt:" + username);
    }

    /**
     * 根据 jwt 获取用户名
     *
     * @param jwt JWT
     * @return 用户名
     */
    public String getUsernameFromJWT(String jwt) {
        Claims claims = parseJWT(jwt);
        return claims.getSubject();
    }

    /**
     * 从 request 的 header 中获取 JWT
     *
     * @param request 请求
     * @return JWT
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
