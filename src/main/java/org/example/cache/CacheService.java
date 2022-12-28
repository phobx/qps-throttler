package org.example.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// You review the cache service written by other developer.
// Find issues in code.
// Propose fix and/or improvements
public class CacheService {

    private final ExternalService externalService = new ExternalService();

    private final Map<StringArrayWrapper, List<String>> cache = new ConcurrentHashMap<>();

    public List<String> get(String[] key) {
        StringArrayWrapper stringArrayWrapper = new StringArrayWrapper(key);
        if (cache.containsKey(stringArrayWrapper)) {
            return cache.get(stringArrayWrapper);
        }
        var response = externalService.get(key);
        if (response != null) {
            cache.put(stringArrayWrapper, response);
        }
        return response;
    }
}
