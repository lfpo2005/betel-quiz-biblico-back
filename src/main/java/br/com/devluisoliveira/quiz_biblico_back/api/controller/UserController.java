package br.com.devluisoliveira.quiz_biblico_back.api.controller;

import br.com.devluisoliveira.quiz_biblico_back.core.port.in.UserPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserPortIn userPortIn;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/userDetails")
    public OAuth2User userDetails(@AuthenticationPrincipal OAuth2User principal) {
        return principal;
    }
}
