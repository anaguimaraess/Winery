package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.pedido.Pedido;
import br.com.ecommerce.winery.models.pedido.formasPagamento.CartaoDeCredito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<CartaoDeCredito,Integer> {
}
