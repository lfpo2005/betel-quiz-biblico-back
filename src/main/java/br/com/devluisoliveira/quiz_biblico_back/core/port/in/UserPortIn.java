package br.com.devluisoliveira.quiz_biblico_back.core.port.in;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPortIn {

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    User save(User user);
}