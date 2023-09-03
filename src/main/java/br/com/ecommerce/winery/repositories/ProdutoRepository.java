package br.com.ecommerce.winery.repositories;
import br.com.ecommerce.winery.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto,Integer> {
    
}
