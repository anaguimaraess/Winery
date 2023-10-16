package br.com.ecommerce.winery.models.produtos;
import br.com.ecommerce.winery.models.produtos.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imagem_id;
    @Column(name = "url_imagem")
    private String url;
    @Column(name = "imagem_principal")
    private boolean imagemPrincipal;
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
}
