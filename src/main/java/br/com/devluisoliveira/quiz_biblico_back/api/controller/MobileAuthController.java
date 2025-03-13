package br.com.devluisoliveira.quiz_biblico_back.api.controller;

import br.com.devluisoliveira.quiz_biblico_back.core.port.in.MobileAuthPortIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/mobile")
@RequiredArgsConstructor
public class MobileAuthController {

    private final MobileAuthPortIn mobileAuthPortIn;

    @PostMapping("/auth/google")
    public ResponseEntity<AuthResponse> authenticateWithGoogle(@RequestBody GoogleTokenRequest request) {
        log.info("[CONTROLLER] - Autenticando com Google");

        // Controller apenas delega para o serviço, sem validações ou regras de negócio
        MobileAuthPortIn.AuthResult result = mobileAuthPortIn.authenticateWithGoogle(request.getIdToken());

        return ResponseEntity.ok(new AuthResponse(result.getToken(), result.getUser()));
    }

    public static class GoogleTokenRequest {
        private String idToken;

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        @Override
        public String toString() {
            return "GoogleTokenRequest{Token de tamanho=" + (idToken != null ? idToken.length() : 0) + "}";
        }
    }

    public static class AuthResponse {
        private final String token;
        private final Object user;

        public AuthResponse(String token, Object user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public Object getUser() {
            return user;
        }
    }
}