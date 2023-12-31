package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.backoffice.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findAll();
    List<Usuario> findByNomeContains(String nome);

    boolean existsByEmail(String email);
    Usuario findByEmail(String email);

}
