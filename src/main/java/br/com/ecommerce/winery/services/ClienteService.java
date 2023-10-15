package br.com.ecommerce.winery.services;
import br.com.ecommerce.winery.models.Cliente;
import br.com.ecommerce.winery.models.Endereco;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.ClienteRepository;
import jdk.jfr.ValueDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@Slf4j
public class ClienteService {



    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ValidacaoUtils validacaoUtils;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public void atualizarCliente(Cliente cliente) throws BusinessException {
        Cliente clienteCadastrado = clienteRepository.findById(cliente.getIdCliente()).orElse(null);
        if(clienteCadastrado == null){
            log.error("Cliente não encontrado!");
            throw new BusinessException("Cliente não encontrado!");
        }
        if(validacaoUtils.clienteValido(cliente)){

            clienteCadastrado.setNomeCliente(cliente.getNomeCliente());
            clienteCadastrado.setDataNascimento(cliente.getDataNascimento());
            clienteCadastrado.setGenero(cliente.getGenero());

            validacaoUtils.validarSenhasIguais(cliente.getSenha(), cliente.getConfirmaSenha());
            if (cliente.getSenha().isBlank() || cliente.getSenha().isEmpty()) {
                cliente.setSenha(clienteCadastrado.getSenha());
                cliente.setConfirmaSenha(clienteCadastrado.getConfirmaSenha());
            } else {
                String senha = passwordEncoder.encode(cliente.getSenha());
                clienteCadastrado.setSenha(senha);
                clienteCadastrado.setConfirmaSenha(senha);
            }
            log.info("Cliente atualizado com sucesso!");
            clienteRepository.save(clienteCadastrado);
        }else{
            log.error("Cliente inválido!");
            throw new BusinessException ("Dados do cliente inválidos!");
        }
    }


    public void incluirEndereco(Cliente cliente, Endereco endereco){
        cliente.getEndereco().add(endereco);
        clienteRepository.save(cliente);
    }

}
