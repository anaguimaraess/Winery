package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.produtos.Imagem;
import br.com.ecommerce.winery.models.produtos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Integer> {

    List<Imagem> findByImagemPrincipal(Produto produto);

    List<Imagem> findAllByProduto(Produto produto);

    Optional<Imagem> findById(int id);

}
