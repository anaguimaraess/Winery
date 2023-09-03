package br.com.ecommerce.winery.repositories;
import br.com.ecommerce.winery.models.Produto;
import br.com.ecommerce.winery.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto,Integer> {
    List<Produto> findAll();
}
