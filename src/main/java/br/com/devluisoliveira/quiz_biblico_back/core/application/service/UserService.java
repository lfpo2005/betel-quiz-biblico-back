package br.com.devluisoliveira.quiz_biblico_back.core.application.service;

import br.com.devluisoliveira.quiz_biblico_back.core.domain.User;
import br.com.devluisoliveira.quiz_biblico_back.core.port.out.UserPortOut;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService {

    private final UserPortOut userPortOut;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Extrair informações do usuário
        String googleId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String pictureUrl = oauth2User.getAttribute("picture");

        // Procurar usuário ou criar novo
        User user = userPortOut.findByGoogleId(googleId)
                .orElse(new User());

        // Atualizar informações
        user.setGoogleId(googleId);
        user.setEmail(email);
        user.setName(name);
        user.setPictureUrl(pictureUrl);

        // Salvar usuário
        userPortOut.save(user);

        return oauth2User;
    }
}