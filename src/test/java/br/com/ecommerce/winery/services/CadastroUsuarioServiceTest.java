package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

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

    @Test
    public void testInativarUsuario() throws BusinessException {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setNome("Mikami");
        usuarioExistente.setEmail("mikami@example.com");
        usuarioExistente.setStatus(Status.ATIVO);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        Usuario resultado = cadastroUsuarioService.inativarUsuario(1);

        assertNotNull(resultado);
        assertEquals(Status.INATIVO, resultado.getStatus());

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    public void testReativarUsuario() throws BusinessException {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setNome("Mikami");
        usuarioExistente.setEmail("mikami@example.com");
        usuarioExistente.setStatus(Status.INATIVO);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        Usuario resultado = cadastroUsuarioService.reativarUsuario(1);

        assertNotNull(resultado);
        assertEquals(Status.ATIVO, resultado.getStatus());

        verify(usuarioRepository).save(any(Usuario.class));
    }
}
