package br.com.ecommerce.winery.models.pedido.formasPagamento;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CartaoDeCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Remova a referência bidirecional
    // @OneToOne(mappedBy = "cartaoDeCredito")
    // private Pedido pedido;

    private int pedidoId; // Adicione um campo para armazenar o ID do pedido

    private String numero;
    private String nome;
    private String validade;
    private String cvv;

    @Override
    public String toString() {
        return "CartaoDeCredito{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", nome='" + nome + '\'' +
                ", validade='" + validade + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
