package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.cliente.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {


}
