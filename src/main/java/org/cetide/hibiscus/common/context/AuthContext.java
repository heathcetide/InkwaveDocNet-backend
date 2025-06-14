package org.cetide.hibiscus.common.context;

import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;

public class AuthContext {

    private static final ThreadLocal<UserEntity> currentUser = new ThreadLocal<>();

    private static final ThreadLocal<String> currentToken = new ThreadLocal<>();

    public static void setCurrentUser(UserEntity user) {
        currentUser.set(user);
    }

    public static UserEntity getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentToken(String token) {
        currentToken.set(token);
    }

    public static String getCurrentToken() {
        return currentToken.get();
    }

    public static void clear() {
        currentUser.remove();
        currentToken.remove();
    }
} 