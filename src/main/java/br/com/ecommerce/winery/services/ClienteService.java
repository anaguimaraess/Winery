package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.backoffice.Grupo;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.cliente.FlagEndereco;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.ClienteRepository;
import br.com.ecommerce.winery.repositories.EnderecoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;


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
            try {
                validacaoUtils.validarNomeCliente(cliente.getNome());
                clienteCadastrado.setNome(cliente.getNome());
            } catch (Exception e) {
                throw new BusinessException("Nome inválido");
            }

            Date dataNascimento = cliente.getDataNascimento();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataNascimento);
            calendar.add(Calendar.DATE, 1);
            Date novaDataNascimento = calendar.getTime();
            clienteCadastrado.setDataNascimento(novaDataNascimento);
            clienteCadastrado.setGenero(cliente.getGenero());

            log.info("Cliente atualizado com sucesso!");
            clienteRepository.save(clienteCadastrado);
        } else {
            log.error("Cliente inválido!");
            throw new BusinessException("Dados do cliente inválidos!");
        }
        return clienteCadastrado;
    }

    public void atualizaSenha(Cliente cliente, String novaSenha, String novaConfirmasenha) throws BusinessException {
        validacaoUtils.validarSenhasIguais(novaSenha, novaConfirmasenha);
        String senhaEncriptada = passwordEncoder.encode(novaSenha);
        cliente.setSenha(senhaEncriptada);
        cliente.setConfirmaSenha(senhaEncriptada);
        clienteRepository.save(cliente);
    }

    public void incluirEndereco(Endereco endereco) throws BusinessException {
        try {
            if (endereco == null) {
                log.error("Endereço contém valores nulos!");
                throw new BusinessException("Endereço nulo!");
            } else {
                endereco.setStatus(Status.ATIVO);
                endereco.setFlagEndereco(FlagEndereco.ENTREGA);
                endereco.setPrincipal(false);
                enderecoRepository.save(endereco);
                log.info("Endereço adicionado com sucesso!");

            }
        } catch (BusinessException e) {
            log.error("Erro ao adicionar endereço!");
            throw new BusinessException("Erro ao adicionar endereço!");
        }
    }

    public Cliente cadastrarCliente(Cliente cliente) throws BusinessException {

        System.out.println(cliente);
        validacaoUtils.validarSenhasIguais(cliente.getSenha(), cliente.getConfirmaSenha());
        String senha = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senha);
        cliente.setConfirmaSenha(senha);
        validacaoUtils.validarEmailUnicoCliente(cliente.getEmail());
        validacaoUtils.validarCpf(cliente.getCpf());
        validacaoUtils.validarNomeCliente(cliente.getNome());
        validacaoUtils.validarCEPCliente(cliente);

        cliente.setGrupo(Grupo.CLIENTE);
        if (cliente.getEnderecos().size() == 1) {
            cliente.getEnderecos().get(0).setFlagEndereco(FlagEndereco.ENTREGA);
            cliente.getEnderecos().get(0).setStatus(Status.ATIVO);
            cliente.getEnderecos().get(0).setPrincipal(true);

        } else {
            cliente.getEnderecos().get(0).setFlagEndereco(FlagEndereco.FATURAMENTO);
            cliente.getEnderecos().get(1).setFlagEndereco(FlagEndereco.ENTREGA);
            cliente.getEnderecos().get(0).setStatus(Status.ATIVO);
            cliente.getEnderecos().get(1).setStatus(Status.ATIVO);
            cliente.getEnderecos().get(0).setPrincipal(false);
            cliente.getEnderecos().get(1).setPrincipal(true);

        }
        if (validacaoUtils.clienteValido(cliente)) {
            cliente.setDataNascimento(cliente.getDataNascimento());
        }
        log.info("Cliente cadastrado com sucesso.");
        List <Endereco> enderecos = cliente.getEnderecos();

        cliente.setEnderecos(null);
        Cliente cl = clienteRepository.save(cliente);

        for (Endereco ed : enderecos){
            ed.setCliente(cl);
            enderecoRepository.save(ed);
        }

        return cl;
    }

    public Cliente obterClientePorId(int clienteId) {
        return clienteRepository.findById(clienteId).orElse(null);
    }

    public Cliente obterClientePorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public List<Endereco> obterEnderecos(int clienteId) {
        System.out.println(clienteRepository.findEnderecoByIdCliente(clienteId));
        return clienteRepository.findEnderecoByIdCliente(clienteId);
    }

    public void desativarEndereco(int idEndereco) {
        enderecoRepository.updateStatusToInactiveById(idEndereco);

    }

    public void definirPadrao(int idEndereco, Cliente cliente){
        enderecoRepository.updatePrincipalById(idEndereco, true);
    }
}
