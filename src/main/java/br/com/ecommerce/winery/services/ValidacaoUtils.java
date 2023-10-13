package br.com.ecommerce.winery.services;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.ecommerce.winery.models.Cliente;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Objects;

@Slf4j
public class ValidacaoUtils {

    @Autowired
    private final CPFValidator cpfValidator = new CPFValidator();
    @Autowired
    private UsuarioRepository usuarioRepository;

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

    void validarEmailUnico(String email) throws BusinessException {
        if (usuarioRepository.existsByEmail(email)) {
            log.error("Não é possível cadastrar usuário. O email já está em uso.");
            throw new BusinessException("O email já está em uso.");
        }
    }

    public boolean clienteValido(Cliente cliente) {
        Date dataNascimento = cliente.getDataNascimento();
        Date dataAtual = new Date();

        if (dataNascimento != null || dataNascimento.before(dataAtual)) {
            log.error("Data inserida inválida!");
            return false;
        }
        return true;
    }
}
