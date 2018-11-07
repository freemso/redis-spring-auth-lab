package edu.fudan.repository.impl;

import edu.fudan.config.Constants;
import edu.fudan.domain.TokenEntry;
import edu.fudan.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Token repository implementation using Redis
 */
@Repository
public class RedisTokenRepositoryImpl implements TokenRepository {

    private RedisTemplate<Long, String> redis;

    @Autowired
    public RedisTokenRepositoryImpl(RedisTemplate<Long, String> redis) {
        this.redis = redis;
    }

    public TokenEntry createToken(long userId) {
        // 使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenEntry tokenEntry = new TokenEntry(userId, token);
        // 存储到redis并设置过期时间
        redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return tokenEntry;
    }

    public TokenEntry getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            return null;
        }
        // 使用userId和源token简单拼接成的token，可以增加加密措施
        long userId = Long.parseLong(param[0]);
        String token = param[1];
        return new TokenEntry(userId, token);
    }

    @Override
    public String getAuthentication(TokenEntry tokenEntry) {
        return tokenEntry.getId() + "_" + tokenEntry.getToken();
    }

    public boolean checkToken(TokenEntry tokenEntry) {
        if (tokenEntry == null) {
            return false;
        }
        String token = redis.boundValueOps(tokenEntry.getId()).get();
        if (token == null || !token.equals(tokenEntry.getToken())) {
            return false;
        }

        // 如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redis.boundValueOps(tokenEntry.getId()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    public void deleteToken(long userId) {
        redis.delete(userId);
    }
}
