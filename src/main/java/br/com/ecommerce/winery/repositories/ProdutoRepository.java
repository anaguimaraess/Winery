package br.com.ecommerce.winery.repositories;
import br.com.ecommerce.winery.models.Produto;
import br.com.ecommerce.winery.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Integer> {
    List<Produto> findAll();

    List<Produto> findByNomeProdutoContainingIgnoreCase(String nomeProduto);

}
