package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findAll();

    List<Usuario> findByNomeContains(String nome);

    //List<Usuario> findByNomeEmailStatusGrupo(String nome, String email, Grupo grupo, Status status);
    boolean existsByEmail(String email);

}
