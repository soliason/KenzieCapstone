package com.kenzie.appserver.config;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.Recipe;

import java.util.concurrent.TimeUnit;

public class CacheStore {

    private Cache<String, Recipe> cache;

    public CacheStore(int expiry, TimeUnit timeUnit){
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public Recipe get(String recipeId){
        return cache.getIfPresent(recipeId);
    }

    public void evict(String recipeId){
        cache.invalidate(recipeId);
    }

    public void add(String recipeId, Recipe recipe){
        cache.put(recipeId, recipe);
    }
}
