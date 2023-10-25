//package br.com.ecommerce.winery.services;
//
//import br.com.ecommerce.winery.models.cliente.Cliente;
//import br.com.ecommerce.winery.models.cliente.Endereco;
//import br.com.ecommerce.winery.models.pedido.Pedido;
//import br.com.ecommerce.winery.models.pedido.ProdutosPedido;
//import br.com.ecommerce.winery.models.pedido.StatusPedido;
//
//public class PedidoService {
//
//    public void criarPedido(Pedido pedidoCliente){
//        Pedido pedido = new Pedido();
//        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
//        pedido.setCarrinho(pedidoCliente.getCarrinho());
//    }
//    public void adicionarProduto(){
//
//    }
//
//    public void removerProduto(){
//
//    }
//
//    public void alterarQuantidadeProduto(){
//
//    }
//
//    public void calcularSubtotal(Pedido pedido){
//        double subtotal = 0.0;
//
//        for (ProdutosPedido item : pedido.getCarrinho()) {
//            int quantidade = item.getQuantidade();
//            double valorProduto = item.getProduto().getPrecoProduto();
//            subtotal += quantidade * valorProduto;
//        }
//    }
//    public void calcularTotal(Pedido pedido, Cliente cliente){
//        calcularSubtotal(pedido);
//
//    }
//
//
//}
