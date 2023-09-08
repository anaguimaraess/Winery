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
    @Column(name = "id_produto")
    private int idProduto;
    @Column(name = "nome_produto")
    private String nomeProduto;
    @Column(name = "avaliacao")
    private float avaliacaoProduto;
    @Column(name = "descricao_detalhada")
    private String descricaoProduto;
    @Column(name = "preco")
    private double precoProduto;
    @Column(name = "qtd_estoque")
    private int qtdEstoque;
    @Enumerated(EnumType.STRING)
    private Status statusProduto;
}
