package br.com.devluisoliveira.quiz_biblico_back.core.port.out;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPortOut {

    Optional<User> findById(UUID id);

    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByEmail(String email);

    User save(User user);
}