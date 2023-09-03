package br.com.ecommerce.winery.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int produto_id;
    @Column(name = "nome_produto")
    private String nomeProduto;
    @Column(name = "avaliacao")
    private float avaliacaoProduto;
    @Column(name = "descricao_detalhada")
    private String descricaoProduto;
    @Column(name = "preco_produto")
    private double precoProduto;
    @Column(name = "qtd_estoque")
    private int qtdEstoque;

}
