package org.cetide.hibiscus.domain.factory;


import org.cetide.hibiscus.domain.model.aggregate.User;

import java.time.LocalDateTime;

public class UserFactory {

    public static User createNewUser(String username, String email, String password) {
        return new User(
                null,
                username,
                email,
                password,
                null, // 默认头像
                "ACTIVE",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
    }
}