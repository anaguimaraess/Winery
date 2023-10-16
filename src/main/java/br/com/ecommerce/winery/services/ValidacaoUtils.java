package br.com.ecommerce.winery.services;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.cliente.ViaCEP;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.ClienteRepository;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ValidacaoUtils {

    @Autowired
    private final CPFValidator cpfValidator = new CPFValidator();
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;


    public boolean validarCpf(String cpf) throws BusinessException {
        try {
            cpfValidator.assertValid(cpf);
            System.out.println("CPF válido.");
            return true;
        } catch (InvalidStateException e) {
            throw new BusinessException("CPF inválido.");
        }
    }

    public void validarSenhasIguais(String senha, String confirmacaoSenha) throws BusinessException {
        if (!Objects.equals(senha, confirmacaoSenha)) {
            log.error("As senhas não coincidem entre si.");
            throw new BusinessException("Senhas não coincidem");
        }
    }

    public void validarEmailUnico(String email) throws BusinessException {
        if (usuarioRepository.existsByEmail(email)) {
            log.error("Não é possível cadastrar usuário. O email já está em uso.");
            throw new BusinessException("O email já está em uso.");
        }
    }

    private static boolean enderecoCompleto(Endereco endereco) {
        return endereco != null &&
                endereco.getLogradouro() != null && !endereco.getLogradouro().isEmpty() &&
                endereco.getNumero() > 0 &&
                endereco.getCidade() != null && !endereco.getCidade().isEmpty() &&
                endereco.getEstado() != null && !endereco.getEstado().isEmpty() &&
                endereco.getCep() != null && !endereco.getCep().isEmpty() &&
                endereco.getUf() != null && !endereco.getUf().isEmpty();
    }

    public static void validarEnderecoCompleto(List<Endereco> enderecos) throws BusinessException {
        for (Endereco endereco : enderecos) {
            if (endereco == null || !enderecoCompleto(endereco)) {
                log.error("O endereço completo é obrigatório.");
                throw new BusinessException("O endereço completo é obrigatório.");
            }
        }
    }

    public void validarEmailUnicoCliente(String email) throws BusinessException {
        if (clienteRepository.existsByEmail(email)) {
            log.error("Não é possível cadastrar cliente. O email já está em uso.");
            throw new BusinessException("O email já está em uso.");
        }
    }

    public boolean clienteValido(Cliente cliente) {
        Date dataNascimento = cliente.getDataNascimento();
        Date dataAtual = new Date();

        if (dataNascimento == null || dataNascimento.after(dataAtual)) {
            log.error("Data inserida inválida!");
            return false;
        }
        return true;
    }

    public void validarNomeCliente(String nome) throws BusinessException {
        if (nome == null) {
            log.error("O nome do cliente não pode ser nulo.");
            throw new BusinessException("O nome do cliente não pode ser nulo.");
        }

        String[] palavras = nome.split(" ");

        if (palavras.length < 2) {
            log.error("O nome do cliente deve conter pelo menos duas palavras.");
            throw new BusinessException("O nome do cliente deve conter pelo menos duas palavras.");
        }

        String nomeSemEspacos = nome.replaceAll("\\s+", "");

        if (nomeSemEspacos.length() < 3) {
            log.error("Nome deve conter pelo menos 3 letras.");
            throw new BusinessException("Nome deve conter pelo menos 3 letras.");
        }
    }

    public boolean validarCEP(String cep) throws BusinessException {

        cep = cep.replaceAll("[^0-9]", "");


        if (cep.length() != 8) {
            log.error("CEP inválido.");
            throw new BusinessException("CEP inválido.");
        }

        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        RestTemplate restTemplate = new RestTemplate();
        ViaCEP response = restTemplate.getForObject(url, ViaCEP.class);

        if (response != null && response.getCep() != null) {
            log.info("CEP é valido!");
            return true;
        } else {
            log.error("CEP inválido ou não encontrado.");
            throw new BusinessException("CEP inválido ou não encontrado.");
        }
    }

    public void validarCEPCliente(Cliente cliente) throws BusinessException {
        List<Endereco> enderecos = cliente.getEndereco();

        for (Endereco endereco : enderecos) {
            validarCEP(endereco.getCep());
        }
    }
}
