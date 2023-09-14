package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findAll();

   Produto findByIdProduto(int id);

    List<Produto> findByNomeProdutoContainingIgnoreCase(String nomeProduto);

    Page<Produto> findAllByOrderByIdProdutoDesc(Pageable pageable);

    Page<Produto> findByNomeProdutoContainingIgnoreCaseOrderByIdProdutoDesc(String nomeProduto, Pageable pageable);


}
