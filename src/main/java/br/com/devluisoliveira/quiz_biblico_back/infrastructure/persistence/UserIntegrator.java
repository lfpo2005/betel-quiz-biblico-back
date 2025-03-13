package br.com.devluisoliveira.quiz_biblico_back.infrastructure.persistence;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;
import br.com.devluisoliveira.quiz_biblico_back.core.port.out.UserPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserIntegrator implements UserPortOut {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<User> findById(UUID id) {
        log.info("Buscando usuário por ID: {}", id);
        User user = mongoTemplate.findById(id, User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByGoogleId(String googleId) {
        log.info("Buscando usuário por Google ID: {}", googleId);
        Query query = new Query(Criteria.where("googleId").is(googleId));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Buscando usuário por email: {}", email);
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public User save(User user) {
        log.info("Salvando usuário: {}", user);
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        return mongoTemplate.save(user);
    }
}