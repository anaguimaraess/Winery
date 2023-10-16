package br.com.ecommerce.winery.models.pedido;

import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.pedido.formasPagamento.FormaPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Pedido {
    @Id
    @GeneratedValue
    @Column(name = "id_pedido")
    private int idPedido;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idCliente")
    private Cliente idComprador;
    @OneToMany(cascade = CascadeType.ALL)
    List<ProdutosPedido> carrinho;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    private int frete;
    private double subtotal;
    private double total;
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;
}
