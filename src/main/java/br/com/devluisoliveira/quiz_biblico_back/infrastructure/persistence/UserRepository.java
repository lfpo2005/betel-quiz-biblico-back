package br.com.devluisoliveira.quiz_biblico_back.infrastructure.persistence;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, UUID> {

    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByEmail(String email);
}