package ru.gentleman.quiz.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheClear {

    private final CacheManager cacheManager;

    public void clearAllQuizAttemptsByUserId(UUID userId){
        log.info("allQuizAttemptsByUserId");
        deleteCache("clearAllQuizAttemptsByUserId", userId);
    }

    private void deleteCache(String key, Object value){
        Cache cache = this.cacheManager.getCache(key);
        if(cache != null){
            cache.evict(value);
        }
    }
}
