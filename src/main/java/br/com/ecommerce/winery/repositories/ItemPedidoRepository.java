package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<Pedido.ItemPedido, Integer> {
}
