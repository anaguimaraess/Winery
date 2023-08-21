package br.com.ecommerce.winery.services;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.ecommerce.winery.models.Grupo;
import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.admin.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAtualizarUsuario() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setNome("Guilherme");
        usuarioExistente.setEmail("guilherme@example.com");
        usuarioExistente.setSenha("senhaAntiga");
        usuarioExistente.setGrupo(Grupo.valueOf("ESTOQUISTA"));

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("Mikami");
        usuarioAtualizado.setEmail("mikami@example.com");
        usuarioAtualizado.setSenha("senhaAtual");
        usuarioAtualizado.setGrupo(Grupo.valueOf("ADMIN"));

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        Usuario resultado = usuarioService.atualizarUsuario(1, usuarioAtualizado);

        assertNotNull(resultado);
        assertEquals(usuarioAtualizado.getNome(), resultado.getNome());
        assertEquals(usuarioAtualizado.getEmail(), resultado.getEmail());
        assertEquals(usuarioAtualizado.getSenha(), resultado.getSenha());
        assertEquals(usuarioAtualizado.getGrupo(), resultado.getGrupo());

        verify(usuarioRepository).save(any(Usuario.class));
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

        Usuario resultado = usuarioService.inativarUsuario(1);

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

        Usuario resultado = usuarioService.reativarUsuario(1);

        assertNotNull(resultado);
        assertEquals(Status.ATIVO, resultado.getStatus());

        verify(usuarioRepository).save(any(Usuario.class));
    }

}
