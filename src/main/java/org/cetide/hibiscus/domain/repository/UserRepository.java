package org.cetide.hibiscus.domain.repository;

import org.cetide.hibiscus.domain.model.aggregate.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    void save(User user);
    Optional<User> findById(Long id);
}
