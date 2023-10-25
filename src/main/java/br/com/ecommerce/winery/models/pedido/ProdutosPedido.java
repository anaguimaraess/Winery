//package br.com.ecommerce.winery.models.pedido;
//
//import br.com.ecommerce.winery.models.produtos.Produto;
//import lombok.Data;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "produtos_pedido")
//@Data
//public class ProdutosPedido {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @ManyToOne
//    @JoinColumn(name = "id_pedido")
//    private Pedido pedido;
//
//    @ManyToOne
//    @JoinColumn(name = "id_produto")
//    private Produto produto;
//    private int quantidade;
//}
