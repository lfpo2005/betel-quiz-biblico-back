package br.com.devluisoliveira.quiz_biblico_back.infrastructure.persistence;

import br.com.devluisoliveira.quiz_biblico_back.core.port.out.UserPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserIntegrator implements UserPortOut {

    private final UserRepository userRepository;
}
