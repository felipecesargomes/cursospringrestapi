package curso.api.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import curso.api.rest.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* Estabelece o nosso gerenciador de Token */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    /* Configurando o gerenciador de autenticação */
    protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

        /* Obrigamos a autenticar a URL */
        super(new AntPathRequestMatcher(url));

        /* Obriga a autenticar a url */
        setAuthenticationManager(authenticationManager);
    }


    /* Retorna o usuário ao processar autenticação */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        /* Está pegando o token para validar */

        Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        /* Retorna o usuário login, senha e acessos */
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
    }
}
