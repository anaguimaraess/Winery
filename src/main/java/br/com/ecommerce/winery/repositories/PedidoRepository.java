package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.pedido.Pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {


    List<Pedido> findByIdDoCliente(String idDoCliente);

}
