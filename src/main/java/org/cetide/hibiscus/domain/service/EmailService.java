package org.cetide.hibiscus.domain.service;

public interface EmailService {

    void sendWelcomeEmail(String username, String email) throws Exception;


    void sendEmailWithRetry(String email, String code, int maxRetries, long retryInterval) throws Exception;
}
