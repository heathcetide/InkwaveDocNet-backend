package org.cetide.hibiscus.infrastructure.cache;

import org.cetide.hibiscus.common.constants.CacheConstants;
import org.cetide.hibiscus.interfaces.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCacheService {

    @Autowired
    private CacheManager cacheManager;

    public Optional<UserDTO> getUserFromCache(String userId) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_CACHE);
        if (cache != null) {
            return Optional.ofNullable(cache.get(userId, UserDTO.class));
        }
        return Optional.empty();
    }

    public void putUserInCache(String userId, UserDTO user) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_CACHE);
        if (cache != null) {
            cache.put(userId, user);
        }
    }

    public void evictUserFromCache(String userId) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_CACHE);
        if (cache != null) {
            cache.evict(userId);
        }
    }
}
