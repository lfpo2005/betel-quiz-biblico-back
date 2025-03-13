package br.com.devluisoliveira.quiz_biblico_back.core.port.in;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;

public interface MobileAuthPortIn {

    AuthResult authenticateWithGoogle(String idToken);

    class AuthResult {
        private final String token;
        private final User user;

        public AuthResult(String token, User user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }
    }
}