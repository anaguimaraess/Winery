package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.cliente.FlagEndereco;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.models.pedido.ItemPedido;
import br.com.ecommerce.winery.models.pedido.Pedido;
import br.com.ecommerce.winery.models.pedido.StatusPedido;
import br.com.ecommerce.winery.repositories.CartaoRepository;
import br.com.ecommerce.winery.repositories.EnderecoRepository;
import br.com.ecommerce.winery.repositories.ItemPedidoRepository;
import br.com.ecommerce.winery.repositories.PedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class PedidoService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    @Autowired
    private CartaoRepository cartaoRepository;

    public Cliente obterClienteParaExibicao(String clienteUsername) {
        return clienteService.obterClientePorEmail(clienteUsername);
    }

    public List<Endereco> obterEnderecosAtivosPorCliente(int idCliente) throws BusinessException {
        return enderecoRepository.findByClienteStatusAndFlagEndereco(idCliente, Status.ATIVO, FlagEndereco.ENTREGA);
    }

    public Pedido salvarPedido(Pedido pedido) {
        try {
            List<ItemPedido> itemsPedido = pedido.getItensPedido();

            int idEndereco = pedido.getIdEndereco();
            pedido.setIdEndereco(idEndereco);
            pedido.setItensPedido(null);
            pedido.setCartaoDeCredito(null);
            pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

            pedido.setDataPedido(new Date());
            pedido.setId(0);
            Pedido pedidoSalvo = pedidoRepository.save(pedido);

            pedidoSalvo.setNumeroParcelas(pedido.getNumeroParcelas());

            for (ItemPedido itemPedido : itemsPedido) {
                itemPedido.setId(0);
                itemPedido.setPedido(pedidoSalvo);
                itemPedidoRepository.save(itemPedido);
            }
            return pedidoSalvo;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o pedido: " + e.getMessage());
        }
    }
    public List<Pedido> obterPedidosDoCliente(Cliente cliente) {
        return pedidoRepository.findByIdDoClienteOrderByDataDoPedidoDesc(String.valueOf(cliente.getIdCliente()));
    }
    public Optional<Pedido> obterPedidoPorId(int id) {
        return pedidoRepository.findById(id);
    }

    public Endereco obterEnderecoDoPedido(Pedido pedido) {
        return enderecoRepository.findById(pedido.getIdEndereco()).orElse(null);
    }
}