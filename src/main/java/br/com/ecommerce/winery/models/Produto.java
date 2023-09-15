package br.com.ecommerce.winery.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private int idProduto;
    @Column(name = "nome_produto", length = 200)
    private String nomeProduto;
    @Column(name = "avaliacao")
    private float avaliacaoProduto;
    @Column(name = "descricao_detalhada", length = 2000)
    private String descricaoProduto;
    @Column(name = "preco")
    private double precoProduto;
    @Column(name = "qtd_estoque")
    private int qtdEstoque;
    @Enumerated(EnumType.STRING)
    private Status statusProduto;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<Imagem> imagens;

    public Produto(int id, String nome, double avaliacao, String descricao, double preco, int estoque) {
    }
}
