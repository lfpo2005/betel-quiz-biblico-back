# Autenticação Google em Aplicativo Spring Boot

## Visão Geral

Este projeto implementa autenticação com Google em uma aplicação Spring Boot usando arquitetura hexagonal (Ports & Adapters). Oferece tanto autenticação web via OAuth2 quanto uma API REST para aplicativos móveis React Native.

## Arquitetura

O código segue a arquitetura hexagonal com separação clara entre:

- **API Layer**: Controllers que recebem requisições HTTP
- **Core Layer**: Lógica de negócios e portas de entrada/saída
- **Infrastructure Layer**: Adaptadores para sistemas externos (MongoDB)

## Funcionalidades Implementadas

1. **Autenticação Mobile**:
    - Endpoint REST para validar tokens do Google e emitir JWTs
    - Fluxo completo para aplicativos móveis React Native

2. **Autenticação Web**:
    - Página de login com botão "Entrar com Google"
    - Fluxo OAuth2 integrado ao Spring Security

3. **Segurança**:
    - JWT para autenticação stateless
    - Filtro JWT para validar tokens
    - Tratamento global de exceções

4. **Persistência**:
    - MongoDB para armazenamento de usuários
    - Uso do MongoTemplate para flexibilidade

## Estrutura do Projeto

```
src/main/java/br/com/devluisoliveira/quiz_biblico_back/
├── api/
│   ├── controller/        # Controllers REST
│   └── exception/         # Tratamento global de exceções
├── core/
│   ├── application/
│   │   └── service/       # Implementação das regras de negócio
│   ├── domain/            # Modelos e entidades
│   └── port/
│       ├── in/            # Portas de entrada (interfaces)
│       └── out/           # Portas de saída (interfaces)
├── infrastructure/
│   └── persistence/       # Implementação de persistência (MongoDB)
└── shared/
    └── config/            # Configurações (Spring Security, JWT, etc.)
```

## Pré-requisitos

- Java 17+
- Maven
- MongoDB
- Conta no Google Cloud com OAuth configurado

## Configuração

1. **Configuração do Google Cloud**:
    - Crie um projeto no [Google Cloud Console](https://console.cloud.google.com/)
    - Configure as credenciais OAuth 2.0
    - Adicione os URIs de redirecionamento:
        - `http://localhost:8080/login/oauth2/code/google` (para desenvolvimento)
        - `https://seu-dominio.com/login/oauth2/code/google` (para produção)

2. **Configuração do `application.yml`**:
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             google:
               client-id: SEU_CLIENT_ID_AQUI
               client-secret: SEU_CLIENT_SECRET_AQUI
               scope: email,profile
     data:
       mongodb:
         uri: mongodb://usuario:senha@localhost:27017/quiz_biblico_db?authSource=admin

   jwt:
     secret: chave_secreta_muito_segura_com_pelo_menos_32_caracteres_123456
     expiration: 86400000 # 24 horas em milissegundos
   ```

## Endpoints API

### Autenticação Mobile

```
POST /api/mobile/auth/google
Content-Type: application/json

{
  "idToken": "token-id-do-google-obtido-pelo-app"
}
```

**Resposta**:
```json
{
  "token": "jwt-token-para-uso-nas-requisicoes",
  "user": {
    "id": "uuid-do-usuario",
    "name": "Nome do Usuário",
    "email": "email@example.com",
    "googleId": "google-id",
    "pictureUrl": "https://url-da-foto-de-perfil.com"
  }
}
```

### Usuários

```
GET /api/users/{id}
Authorization: Bearer jwt-token
```

```
GET /api/users/email/{email}
Authorization: Bearer jwt-token
```

## Implementação no App React Native

### Instalação de Dependências

```bash
npm install @react-native-google-signin/google-signin
```

### Configuração

```javascript
import { GoogleSignin } from '@react-native-google-signin/google-signin';

// Configure com seu webClientId do Google Cloud
GoogleSignin.configure({
  webClientId: 'SEU_WEB_CLIENT_ID.apps.googleusercontent.com',
});
```

### Login

```javascript
async function signInWithGoogle() {
  try {
    await GoogleSignin.hasPlayServices();
    const userInfo = await GoogleSignin.signIn();
    
    // Enviar idToken para o backend
    const response = await fetch('https://seu-backend.com/api/mobile/auth/google', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        idToken: userInfo.idToken,
      }),
    });
    
    const authData = await response.json();
    
    // Salvar o token JWT para uso nas requisições
    // Exemplo: AsyncStorage.setItem('authToken', authData.token);
    
    return authData;
  } catch (error) {
    console.error('Google Sign-In Error:', error);
  }
}
```

## Contribuição

Para contribuir com este projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Crie um Pull Request

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo LICENSE.md para detalhes.