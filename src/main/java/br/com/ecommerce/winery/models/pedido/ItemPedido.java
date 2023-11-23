package br.com.ecommerce.winery.models.pedido;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    private int quantidade;
    private String nome;
    private double avaliacao;
    private String descricao;
    private double preco;
    private int estoque;
    private String imagemPrincipal;
}
