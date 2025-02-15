package org.test.project.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static statics.Constants.BASE_CACHE;
import static statics.Constants.CONVERSION_CACHE;

@Component
@Profile("test")
public class ScheduledCacheCleaner {
    @Scheduled(fixedDelay = 5, timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    @CacheEvict(value = {BASE_CACHE, CONVERSION_CACHE}, allEntries = true)
    public void cacheEvict() {
    }
}
