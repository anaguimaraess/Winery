package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ValidacaoUtils validacaoUtils;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Cliente atualizarCliente(Cliente cliente) throws BusinessException {
        Cliente clienteCadastrado = clienteRepository.findById(cliente.getIdCliente()).orElse(null);
        if (clienteCadastrado == null) {
            log.error("Cliente não encontrado!");
            throw new BusinessException("Cliente não encontrado!");
        }
        if (validacaoUtils.clienteValido(cliente)) {

            clienteCadastrado.setNome(cliente.getNome());
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
        } else {
            log.error("Cliente inválido!");
            throw new BusinessException("Dados do cliente inválidos!");
        }
        return clienteCadastrado;
    }


    public void incluirEndereco(Cliente cliente, Endereco endereco) throws BusinessException {
        try {
            if(cliente == null){
                log.error("Cliente nulo");
                throw new BusinessException("Cliente com valores nulos!");
            }

            if (endereco == null) {
                log.error("Endereço contém valores nulos!");
                throw new BusinessException("Endereço nulo!");
            } else {
                List<Endereco> enderecos = cliente.getEnderecos();
                enderecos.add(endereco);
                validacaoUtils.validarCEP(endereco.getCep());
                cliente.setEnderecos(enderecos);
                clienteRepository.save(cliente);
                log.info("Endereço adicionado com sucesso!");
            }
        } catch (BusinessException e) {
            log.error("Erro ao adicionar endereço!");
            throw new BusinessException("Erro ao adicionar endereço!");
        }
    }

    public Cliente cadastrarCliente(Cliente cliente) throws BusinessException {
        validacaoUtils.validarSenhasIguais(cliente.getSenha(), cliente.getConfirmaSenha());
        String senha = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senha);
        cliente.setConfirmaSenha(senha);
        validacaoUtils.validarEmailUnicoCliente(cliente.getEmail());
        validacaoUtils.validarCpf(cliente.getCpf());
        validacaoUtils.validarEnderecoCompleto(cliente.getEnderecos());
        validacaoUtils.validarNomeCliente(cliente.getNome());
        validacaoUtils.validarCEPCliente(cliente);

        if (validacaoUtils.clienteValido(cliente)) {
            cliente.setDataNascimento(cliente.getDataNascimento());
        }
        log.info("Cliente cadastrado com sucesso.");
        return clienteRepository.save(cliente);
    }

    public Cliente obterClientePorId(int clienteId) {
        return clienteRepository.findById(clienteId).orElse(null);
    }


}
