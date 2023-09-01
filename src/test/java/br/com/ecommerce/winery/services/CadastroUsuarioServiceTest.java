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
    private LoginService loginService;

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

    @Test
    public void testAlterarNomeUsuario() throws BusinessException {
        Usuario usuarioCadastrado = new Usuario();
        usuarioCadastrado.setId(1);
        usuarioCadastrado.setNome("Guilherme");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1);
        usuarioAtualizado.setNome("Mikami");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioCadastrado));
        when(usuarioRepository.save(usuarioCadastrado)).thenReturn(usuarioCadastrado);

        Usuario resultado = cadastroUsuarioService.alterarNomeUsuario(1, usuarioAtualizado);

        assertEquals("Mikami", resultado.getNome());

        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).save(usuarioCadastrado);
    }

    @Test
    public void testAlterarNomeUsuarioMesmoNome() throws BusinessException {
        Usuario usuarioCadastrado = new Usuario();
        usuarioCadastrado.setId(1);
        usuarioCadastrado.setNome("Guilherme");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1);
        usuarioAtualizado.setNome("Guilherme");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioCadastrado));

        try {
            cadastroUsuarioService.alterarNomeUsuario(1, usuarioAtualizado);
            fail("Expected BusinessException was not thrown");
        } catch (BusinessException e) {
            assertEquals("Nome não pode ser igual ao anterior!", e.getMessage());
        }

        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void testAlterarCpfUsuario() throws BusinessException {
        Usuario usuarioCadastrado = new Usuario();
        usuarioCadastrado.setId(1);
        usuarioCadastrado.setCpf("12345678900");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1);
        usuarioAtualizado.setCpf("98765432100");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioCadastrado));
        when(usuarioRepository.save(usuarioCadastrado)).thenReturn(usuarioCadastrado);

        Usuario resultado = cadastroUsuarioService.alterarCpfUsuario(1, usuarioAtualizado);

        assertEquals("98765432100", resultado.getCpf());

        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).save(usuarioCadastrado);
    }

    @Test
    public void testAlterarCpfUsuarioMesmoCpf() throws BusinessException {
        Usuario usuarioCadastrado = new Usuario();
        usuarioCadastrado.setId(1);
        usuarioCadastrado.setCpf("12345678900");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1);
        usuarioAtualizado.setCpf("12345678900");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioCadastrado));

        try {
            cadastroUsuarioService.alterarCpfUsuario(1, usuarioAtualizado);
            fail("Expected BusinessException was not thrown");
        } catch (BusinessException e) {
            assertEquals("CPF não pode ser igual ao anterior!", e.getMessage());
        }

        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void testAlterarSenha() throws BusinessException {
        Usuario usuarioCadastrado = new Usuario();
        usuarioCadastrado.setId(1);
        usuarioCadastrado.setSenha("123");
        usuarioCadastrado.setConfirmaSenha("123");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1);
        usuarioAtualizado.setSenha("123456");
        usuarioAtualizado.setConfirmaSenha("123456");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioCadastrado));

        when(passwordEncoder.encode("123")).thenReturn("encodedPassword123");
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword123456");

        Usuario resultado = cadastroUsuarioService.alterarSenha(1, usuarioAtualizado);

        assertEquals("encodedPassword123456", resultado.getSenha());
        assertEquals("encodedPassword123456", resultado.getConfirmaSenha());

        assertEquals("encodedPassword123456", resultado.getSenha());
        assertEquals("encodedPassword123456", resultado.getConfirmaSenha());
        verify(usuarioRepository, times(1)).findById(1);

        verify(passwordEncoder, times(2)).encode(anyString());
        verify(usuarioRepository, times(1)).save(usuarioCadastrado);
    }


    @Test
    public void testAlterarSenhaMesmaSenha() throws BusinessException {
        Usuario usuarioCadastrado = new Usuario();
        usuarioCadastrado.setId(1);
        usuarioCadastrado.setSenha("123");
        usuarioCadastrado.setConfirmaSenha("123");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1);
        usuarioAtualizado.setSenha("123");
        usuarioAtualizado.setConfirmaSenha("123");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioCadastrado));

        try {
            cadastroUsuarioService.alterarSenha(1, usuarioAtualizado);
            fail("Expected BusinessException was not thrown");
        } catch (BusinessException e) {
            assertEquals("A senha não pode ser igual a anterior!", e.getMessage());
        }

        verify(usuarioRepository, times(1)).findById(1);
        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository, never()).save(any());
    }
}
