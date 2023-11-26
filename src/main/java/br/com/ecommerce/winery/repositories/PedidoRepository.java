package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Query("SELECT p FROM Pedido p WHERE p.idDoCliente = :idDoCliente ORDER BY p.id DESC")
    List<Pedido> findByIdDoClienteOrderByDataDoPedidoDesc(@Param("idDoCliente") String idDoCliente);

    @Modifying
    @Query("UPDATE Pedido p SET p.status = :status WHERE p.id = :pedido_Id")
    void atualizarStatus(@Param("pedido_Id") Long pedidoId, @Param("status") String status);

    @Query("SELECT p FROM Pedido p ORDER BY p.id DESC")
    List<Pedido> findAllOrderByIdDesc();

}
