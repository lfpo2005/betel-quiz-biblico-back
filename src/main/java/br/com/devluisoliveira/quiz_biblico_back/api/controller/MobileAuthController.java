package br.com.devluisoliveira.quiz_biblico_back.api.controller;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;
import br.com.devluisoliveira.quiz_biblico_back.core.port.in.MobileAuthPortIn;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/mobile")
public class MobileAuthController {

    private final MobileAuthPortIn mobileAuthPortIn;


    @PostMapping("/auth/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleTokenRequest request) {
        log.info("[CONTROLLER] - Autenticando com Google: {}", request);
        try {
            // Verificar o token do Google (você precisará de uma biblioteca para isso)
            GoogleIdToken idToken = verifyGoogleToken(request.getIdToken());

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extrair informações do usuário
                String googleId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                // Encontrar ou criar usuário
                User user = userRepository.findByGoogleId(googleId)
                        .orElse(new User());

                // Atualizar informações
                user.setGoogleId(googleId);
                user.setEmail(email);
                user.setName(name);
                user.setPictureUrl(pictureUrl);

                // Salvar usuário
                userRepository.save(user);

                // Gerar JWT ou outra forma de token para o app
                String jwtToken = generateJwtToken(user);

                return ResponseEntity.ok(new AuthResponse(jwtToken, user));
            }

            return ResponseEntity.status(401).body("Token inválido");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro na autenticação: " + e.getMessage());
        }
    }

    // Classes auxiliares

    private static class GoogleTokenRequest {
        private String idToken;

        // getters, setters
    }

    private static class AuthResponse {
        private String token;
        private User user;

        public AuthResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }

        // getters, setters
    }

    // Método para verificar o token (implementação simplificada)
    private GoogleIdToken verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList("SEU_CLIENT_ID"))
                    .build();

            return verifier.verify(idTokenString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para gerar JWT (implementação simplificada)
    private String generateJwtToken(User user) {
        // Implemente a geração de JWT aqui
        return "jwt-token-example";
    }
}
