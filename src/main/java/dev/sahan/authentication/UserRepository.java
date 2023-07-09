package dev.sahan.authentication;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByUsername(Object username);
    // Additional query methods can be added here

    boolean existsByEmail(Object email);

    User findByUsernameAndPassword(Object username, Object password);
}
