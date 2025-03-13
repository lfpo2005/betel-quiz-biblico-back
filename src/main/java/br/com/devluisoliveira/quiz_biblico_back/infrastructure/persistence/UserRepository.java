package br.com.devluisoliveira.quiz_biblico_back.infrastructure.persistence;


import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByEmail(String email);
}