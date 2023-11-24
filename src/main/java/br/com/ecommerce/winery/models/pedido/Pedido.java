package br.com.ecommerce.winery.models.pedido;

import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.pedido.formasPagamento.CartaoDeCredito;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Data
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private int id;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private List<ItemPedido> itensPedido;
    @Column(name = "idEndereco")
    private int idEndereco;

//    @ManyToOne
//    @JoinColumn(name = "idEndereco", insertable = false, updatable = false, nullable = true)
//    private Endereco endereco;
    @JoinColumn(name = "cartao_id")
    @OneToOne(cascade = CascadeType.ALL)
    private CartaoDeCredito cartaoDeCredito;
    private int numeroParcelas;
    private String idDoCliente;
    private double valorTotal;
    private double valorFretePedido;
    private String formaPagamento;
    @Temporal(TemporalType.DATE)
    private Date dataPedido;

    public Pedido(int id, StatusPedido status, List<ItemPedido> itensPedido, int idEndereco,  CartaoDeCredito cartaoDeCredito, int numeroParcelas, String idDoCliente, double valorTotal, double valorFretePedido, String formaPagamento, Date dataPedido) {
        this.id = id;
        this.status = status;
        this.itensPedido = itensPedido;
        this.idEndereco = idEndereco;

        this.cartaoDeCredito = cartaoDeCredito;
        this.numeroParcelas = numeroParcelas;
        this.idDoCliente = idDoCliente;
        this.valorTotal = valorTotal;
        this.valorFretePedido = valorFretePedido;
        this.formaPagamento = formaPagamento;
        this.dataPedido = dataPedido;
    }

    public Pedido(){};

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", status=" + status +
                ", itensPedido=" + itensPedido +
                ", idEndereco=" + idEndereco +
                ", cartaoDeCredito=" + cartaoDeCredito +
                ", numeroParcelas=" + numeroParcelas +
                ", idDoCliente='" + idDoCliente + '\'' +
                ", valorTotal=" + valorTotal +
                ", valorFretePedido=" + valorFretePedido +
                ", formaPagamento='" + formaPagamento + '\'' +
                ", dataPedido=" + dataPedido +
                '}';
    }
}
