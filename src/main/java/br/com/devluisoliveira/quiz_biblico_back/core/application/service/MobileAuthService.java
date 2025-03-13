package br.com.devluisoliveira.quiz_biblico_back.core.application.service;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;
import br.com.devluisoliveira.quiz_biblico_back.core.port.in.MobileAuthPortIn;
import br.com.devluisoliveira.quiz_biblico_back.core.port.out.UserPortOut;
import br.com.devluisoliveira.quiz_biblico_back.shared.config.security.JwtTokenUtil;
import br.com.devluisoliveira.quiz_biblico_back.shared.exception.GlobalExceptionHandler;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MobileAuthService implements MobileAuthPortIn {

    private final UserPortOut userPortOut;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Override
    public AuthResult authenticateWithGoogle(String idToken) {
        log.info("Autenticando com Google Token");
        try {
            GoogleIdToken googleIdToken = verifyGoogleToken(idToken);

            if (googleIdToken == null) {
                log.error("Token do Google inválido");
                throw new AuthenticationException("Token do Google inválido ou expirado");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            // Extrair informações do usuário
            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            log.info("Usuário autenticado com Google: {}, {}", name, email);

            // Procurar usuário ou criar novo
            User user = userPortOut.findByGoogleId(googleId)
                    .orElse(new User());

            if (user.getId() == null) {
                user.setId(UUID.randomUUID());
            }

            // Atualizar informações
            user.setGoogleId(googleId);
            user.setEmail(email);
            user.setName(name);
            user.setPictureUrl(pictureUrl);

            // Salvar usuário
            User savedUser = userPortOut.save(user);

            // Gerar JWT
            String jwt = jwtTokenUtil.generateToken(savedUser);

            return new AuthResult(jwt, savedUser);

        } catch (GlobalExceptionHandler.AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao autenticar com Google", e);
            throw new GlobalExceptionHandler.AuthenticationException("Erro ao autenticar com Google: " + e.getMessage());
        }
    }

    private GoogleIdToken verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            return verifier.verify(idTokenString);
        } catch (Exception e) {
            log.error("Erro ao verificar token do Google", e);
            return null;
        }
    }
}