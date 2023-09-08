package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.Imagem;
import br.com.ecommerce.winery.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Integer> {

    List<Imagem> findByImagemPrincipal(Produto produto);
}