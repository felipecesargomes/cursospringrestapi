package curso.api.rest.controller;

import curso.api.rest.model.Telefone;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

    @Autowired /* se fosse CDI seria @Inject */
    private UsuarioRepository usuarioRepository;

    @GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/json")
    public ResponseEntity<Usuario> relatorio(@PathVariable (value = "id") Long id, @PathVariable (value = "venda") Long venda) {

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity(usuario.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> init(@PathVariable (value = "id") Long id) {

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity(usuario.get(), HttpStatus.OK);
    }

    @GetMapping(value="/", produces = "application/json")
    public ResponseEntity<List<Usuario>> consultarTodos() {

        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

        System.out.println("teste");
        for(Telefone t : usuario.getTelefones()) {
            t.setUsuario(usuario);
        }

        String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
        System.out.println(senhacriptografada);
        usuario.setSenha(senhacriptografada);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
    }

    @PostMapping(value="/{iduser}/idvenda/{idvenda}", produces="application/json")
    public ResponseEntity<Usuario> cadastrarVenda(@PathVariable Long iduser, @PathVariable Long idvenda) {
        return new ResponseEntity("id user: "+ iduser + " idvenda : " + idvenda, HttpStatus.OK );
    }


    @DeleteMapping(value="/{id}", produces = "application/text")
    public ResponseEntity<Usuario> deletar(@PathVariable("id") Long id) {
        usuarioRepository.deleteById(id);
        return new ResponseEntity("Deletado com Sucesso!", HttpStatus.OK);
    }

    @PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> alterar(@RequestBody Usuario usuario) {

        for (Telefone t : usuario.getTelefones()) {
            t.setUsuario(usuario);
        }

        Usuario userTemporario = usuarioRepository.findUSerByLogin(usuario.getLogin());

        String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
        String senhaBanco = userTemporario.getSenha();

        if (!(senhaBanco.equals(senhacriptografada))) {

            usuario.setSenha(senhacriptografada);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
    }

}