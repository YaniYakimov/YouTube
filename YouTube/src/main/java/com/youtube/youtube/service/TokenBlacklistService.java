package com.youtube.youtube.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
@Service
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());

    public void addToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
