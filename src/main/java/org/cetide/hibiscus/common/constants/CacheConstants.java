package org.cetide.hibiscus.common.constants;

import java.time.Duration;

public interface CacheConstants {
    String USER_CACHE = "userCache";
    Duration DEFAULT_TTL = Duration.ofMinutes(10);
}
