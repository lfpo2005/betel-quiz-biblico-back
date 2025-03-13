package br.com.devluisoliveira.quiz_biblico_back.core.application.service;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;
import br.com.devluisoliveira.quiz_biblico_back.core.port.in.UserPortIn;
import br.com.devluisoliveira.quiz_biblico_back.core.port.out.UserPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserPortIn {

    private final UserPortOut userPortOut;

    @Override
    public Optional<User> findById(UUID id) {
        log.info("Buscando usuário por ID: {}", id);
        return userPortOut.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Buscando usuário por e-mail: {}", email);
        return userPortOut.findByEmail(email);
    }

    @Override
    public User save(User user) {
        log.info("Salvando usuário: {}", user);
        return userPortOut.save(user);
    }
}