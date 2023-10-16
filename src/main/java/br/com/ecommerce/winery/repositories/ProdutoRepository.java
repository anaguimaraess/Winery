package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.Produto;
import br.com.ecommerce.winery.models.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findAll();

    Produto findByIdProduto(int id);

    Page<Produto> findAllByOrderByIdProdutoDesc(Pageable pageable);

    Page<Produto> findByNomeProdutoContainingIgnoreCaseOrderByIdProdutoDesc(String nomeProduto, Pageable pageable);

    @Query("SELECT p FROM Produto p WHERE p.statusProduto = :status ORDER BY p.idProduto DESC")
    Page<Produto> findAllByOrderByIdProdutoDescAndStatus(@Param("status") Status status, Pageable pageable);


}
