package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Cliente findByNome(String nomeCliente);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    Cliente findByEmail(String email);


}
