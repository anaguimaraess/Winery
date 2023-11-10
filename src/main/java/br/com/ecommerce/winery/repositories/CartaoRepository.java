package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Pedido.CartaoDeCredito,Integer> {
}
