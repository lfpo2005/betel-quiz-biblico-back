package br.com.devluisoliveira.quiz_biblico_back.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para a página de login web
 * Fornece uma interface básica para login via OAuth2
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
