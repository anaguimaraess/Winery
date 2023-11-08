package br.com.ecommerce.winery.repositories;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.cliente.FlagEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Endereco e SET e.status = 'INATIVO' WHERE e.idEndereco = :enderecoId")
    void updateStatusToInactiveById(@Param("enderecoId") int enderecoId);


    @Query("SELECT e FROM Endereco e WHERE " +
            "(:clienteId IS NULL OR e.cliente.idCliente = :clienteId) " +
            "AND (:status IS NULL OR e.status = :status) " +
            "AND (:flagEndereco IS NULL OR e.flagEndereco = :flagEndereco)")
    List<Endereco> findByClienteStatusAndFlagEndereco(
            @Param("clienteId") Integer clienteId,
            @Param("status") Status status,
            @Param("flagEndereco") FlagEndereco flagEndereco
    );


    @Transactional
    @Modifying
    @Query("UPDATE Endereco e SET e.principal = :principal, e.flagEndereco = :flagEndereco WHERE e.cliente = :cliente")
    void updateEnderecosByClienteAndPrincipalAndFlag(@Param("cliente") Cliente cliente,
                                                     @Param("principal") boolean principal,
                                                     @Param("flagEndereco") FlagEndereco flagEndereco);




    @Transactional
    @Modifying
    @Query("UPDATE Endereco e SET e.principal = :principal WHERE e.idEndereco = :idEndereco")
    void updatePrincipalById(@Param("idEndereco") int id, @Param("principal") boolean principal);

}
