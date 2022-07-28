package curso.api.rest.repository;

import curso.api.rest.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    @Query("Select u from Usuario u where u.login = ?1")
    Usuario findUSerByLogin(String login);

}
