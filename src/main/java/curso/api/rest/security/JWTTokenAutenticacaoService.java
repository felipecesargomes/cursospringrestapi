package curso.api.rest.security;

import curso.api.rest.cursospringrestapi.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
@Component
public class JWTTokenAutenticacaoService {

    /* Tempo de validade do TOKEN */
    private static final long EXPIRATION_TIME = 172800000;

    /* Uma senha unica para compor a autenticação e ajudar na segurança */
    private static final String SECRET = "SenhaExtremamenteSecreta";

    /* Prefixo padrão de Token */
    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    /* Gerando token de autenticação e adicionando ao cabeçalho e resposta Http */
    public void addAuthentication(HttpServletResponse response, String username) throws IOException {
        /* Montagem do Token */
        String JWT = Jwts.builder() /* Chama o gereador de Token */
                .setSubject(username) /* adiciona o usuário */
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Tempo de expiração */
                .signWith(SignatureAlgorithm.HS512, SECRET).compact(); /* Compactação e algoritmo de geração de senha */

        /* Junta token com o prefixo */
        String token = TOKEN_PREFIX + " " + JWT; /* Bearer 8372737272277277edsdf2732 */

        /* Adiciona no cabeççalho http */
        response.addHeader(HEADER_STRING, token); /* Authorization: Bearer 3243242rueshfu322 */

        /* Escreve token como resposta no corpo do http */
        response.getWriter().write("{\"Authorization\": \""+token+"\"}");

        /* Retorna o usuário validado com token ou caso não seja validado retorna null */

    }

    public Authentication getAuthentication(HttpServletRequest request) {
        /* Pega o token enviado no cabeçalho http */
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            /* Faz a validação do token do usuário na requisição */
            String user = Jwts.parser().setSigningKey(SECRET) /* Bearer 87878we8we787w8e78wa7s878 */
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
            if(user != null) {
                Usuario usuario = ApplicationContextLoad.getApplicationContext()
                        .getBean(UsuarioRepository.class).findUSerByLogin(user);
                /* Retornar o usuário Logado */

                if(usuario != null) {
                    return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
                }
            }
        }
        return null;
    }
}
