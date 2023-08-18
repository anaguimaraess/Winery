package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.admin.CadastroUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class CadastroUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CadastroUsuarioService cadastroUsuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarUsuario_SenhasNaoCoincidem_Exception() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha1");
        usuario.setConfirmaSenha("senha2");

        assertThrows(BusinessException.class, () -> cadastroUsuarioService.cadastrarUsuario(usuario));
    }

    @Test
    public void testCadastrarUsuario_EmailJaExistente_Exception() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@example.com");

        when(usuarioRepository.existsByEmail("email@example.com")).thenReturn(true);

        when(passwordEncoder.matches("senha", "senha")).thenReturn(false);

        assertThrows(BusinessException.class, () -> cadastroUsuarioService.cadastrarUsuario(usuario));
    }


    @Test
    public void testCadastrarUsuario_Sucesso() throws BusinessException {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");
        usuario.setConfirmaSenha("senha123");
        usuario.setEmail("novoemail@example.com");

        when(usuarioRepository.existsByEmail("novoemail@example.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");

        Usuario usuarioSalvo = new Usuario();
        when(usuarioRepository.save(any())).thenReturn(usuarioSalvo);

    }
}
