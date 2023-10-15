package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.Cliente;
import br.com.ecommerce.winery.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Cliente findByNome(String nomeCliente);

    boolean existsByEmail(String email);
    Cliente findByEmail(String email);


}
