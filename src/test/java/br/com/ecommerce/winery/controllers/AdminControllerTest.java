//package br.com.ecommerce.winery.controllers;
//
//import br.com.ecommerce.winery.controllers.admin.AdminController;
//import br.com.ecommerce.winery.models.Grupo;
//import br.com.ecommerce.winery.models.Usuario;
//import br.com.ecommerce.winery.models.exception.BusinessException;
//import br.com.ecommerce.winery.repositories.UsuarioRepository;
//import br.com.ecommerce.winery.services.PoderAdminService;
//import br.com.ecommerce.winery.utils.MensagemRetorno;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class AdminControllerTest {
//
//    @InjectMocks
//    private AdminController adminController;
//
//    @Mock
//    private PoderAdminService cadastroUsuarioService;
//
//    @Mock
//    private HttpServletResponse response;
//    @Mock
//    private UsuarioRepository usuarioRepository;
//    @Mock
//    private Model model;
//
//    @Mock
//    private MensagemRetorno mensagemRetorno;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCadastrarUsuario() throws BusinessException {
//        Usuario usuario = new Usuario();
//        usuario.setSenha("senha");
//        usuario.setConfirmaSenha("senha");
//        usuario.setEmail("email@teste.com");
//
//        when(cadastroUsuarioService.cadastrarUsuario(usuario)).thenReturn(new Usuario());
//
//        String result = adminController.cadastrarUsuario(usuario, model, response);
//
//        assertEquals("cadastroUsuario", result);
//        verify(response, times(1)).setStatus(HttpStatus.CREATED.value());
//    }
//
//    @Test
//    public void testCadastrarUsuarioWithException() throws BusinessException {
//        Usuario usuario = new Usuario();
//        usuario.setSenha("senha");
//        usuario.setConfirmaSenha("senha");
//        usuario.setEmail("email@teste.com");
//
//        when(cadastroUsuarioService.cadastrarUsuario(usuario)).thenThrow(new BusinessException("Erro ao cadastrar usuário"));
//
//        String result = adminController.cadastrarUsuario(usuario, model, response);
//
//        assertEquals("cadastroUsuario", result);
//        verify(response, times(1)).setStatus(HttpStatus.BAD_REQUEST.value());
//    }
//
//
//    @Test
//    public void testAtualizarNomeDoUsuarioWithNullName() {
//        Usuario usuario = new Usuario();
//        usuario.setId(1);
//
//        ResponseEntity<?> responseEntity = adminController.atualizarNomeDoUsuario(usuario);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNull(responseEntity.getBody());
//    }
//
//    @Test
//    public void testAtualizarNomeDoUsuarioWithException() throws BusinessException {
//        Usuario usuario = new Usuario();
//        usuario.setId(1);
//        usuario.setNome("Novo Nome");
//
//        when(cadastroUsuarioService.buscarUsuarioPorId(1)).thenThrow(new BusinessException("Erro ao buscar usuário"));
//
//        ResponseEntity<?> responseEntity = adminController.atualizarNomeDoUsuario(usuario);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//    }
//
//
//    @Test
//    public void testAtualizarCpfDoUsuarioWithNullCpf() {
//        Usuario usuario = new Usuario();
//        usuario.setId(1);
//
//        ResponseEntity<?> responseEntity = adminController.atualizarCpfDoUsuario(usuario);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNull(responseEntity.getBody());
//    }
//
//    @Test
//    public void testAtualizarCpfDoUsuarioWithException() throws BusinessException {
//        Usuario usuario = new Usuario();
//        usuario.setId(1);
//        usuario.setCpf("12345678901");
//
//        when(cadastroUsuarioService.buscarUsuarioPorId(1)).thenThrow(new BusinessException("Erro ao buscar usuário"));
//
//        ResponseEntity<?> responseEntity = adminController.atualizarCpfDoUsuario(usuario);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//    }
//
//
//
//    @Test
//    public void testAtualizarSenhaDoUsuarioWithNullSenha() {
//        Usuario usuario = new Usuario();
//        usuario.setId(1);
//
//        ResponseEntity<?> responseEntity = adminController.atualizarSenhaDoUsuario(usuario);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNull(responseEntity.getBody());
//    }
//
//    @Test
//    public void testAtualizarSenhaDoUsuarioWithException() throws BusinessException {
//        Usuario usuario = new Usuario();
//        usuario.setId(1);
//        usuario.setSenha("novaSenha");
//        usuario.setConfirmaSenha("novaSenha");
//
//        when(cadastroUsuarioService.buscarUsuarioPorId(1)).thenThrow(new BusinessException("Erro ao buscar usuário"));
//
//        ResponseEntity<?> responseEntity = adminController.atualizarSenhaDoUsuario(usuario);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//    }
//
//
//    @Test
//    public void testAtualizarGrupoDoUsuarioWithNullGrupo() {
//        Usuario usuario = new Usuario();
//        usuario.setId(1);
//
//        ResponseEntity<?> responseEntity = adminController.atualizarGrupoDoUsuario(usuario);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNull(responseEntity.getBody());
//    }
//
//    @Test
//    public void testAtualizarGrupoDoUsuarioWithException() throws BusinessException {
//        Usuario usuario = new Usuario();
//        usuario.setId(1);
//        usuario.setGrupo(Grupo.USUARIO);
//
//        when(cadastroUsuarioService.buscarUsuarioPorId(1)).thenThrow(new BusinessException("Erro ao buscar usuário"));
//
//        ResponseEntity<?> responseEntity = adminController.atualizarGrupoDoUsuario(usuario);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//    }
//
//    @Test
//    public void testListarPorId() throws BusinessException {
//        Map<String, Integer> requestBody = new HashMap<>();
//        requestBody.put("id", 1);
//
//        when(cadastroUsuarioService.buscarUsuarioPorId(1)).thenReturn(new Usuario());
//
//        ResponseEntity<Usuario> responseEntity = adminController.listarPorId(requestBody);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//    }
//
//    @Test
//    public void testListarPorIdWithException() throws BusinessException {
//        Map<String, Integer> requestBody = new HashMap<>();
//        requestBody.put("id", 1);
//
//        when(cadastroUsuarioService.buscarUsuarioPorId(1)).thenThrow(new BusinessException("Erro ao buscar usuário"));
//
//        ResponseEntity<Usuario> responseEntity = adminController.listarPorId(requestBody);
//
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        assertNull(responseEntity.getBody());
//    }
//
//    @Test
//    public void testListarTodosUsuarios() {
//        List<Usuario> usuarios = new ArrayList<>();
//        usuarios.add(new Usuario());
//        usuarios.add(new Usuario());
//
//        when(cadastroUsuarioService.listarTodosUsuarios()).thenReturn(usuarios);
//
//        String result = adminController.listarTodosUsuarios(model, response);
//
//        assertEquals("listaUsuario", result);
//        verify(model, times(1)).addAttribute("usuarios", usuarios);
//        verify(response, times(1)).setStatus(HttpStatus.OK.value());
//    }
//
//    @Test
//    public void testAlternarStatusUsuario() throws BusinessException {
//        when(cadastroUsuarioService.alternarStatusUsuario(1)).thenReturn(new Usuario());
//
//        String result = adminController.alternarStatusUsuario(1, model, response);
//
//        assertEquals("redirect:/admin/listar", result);
//        verify(response, times(1)).setStatus(HttpStatus.OK.value());
//    }
//
//    @Test
//    public void testAlternarStatusUsuarioWithException() throws BusinessException {
//        when(cadastroUsuarioService.alternarStatusUsuario(1)).thenThrow(new BusinessException("Erro ao alterar status do usuário"));
//
//        String result = adminController.alternarStatusUsuario(1, model, response);
//
//        assertEquals("listaUsuario", result);
//        verify(response, times(1)).setStatus(HttpStatus.BAD_REQUEST.value());
//    }
//
//    @Test
//    public void testFiltroPorNome() {
//        String nome = "usuario";
//
//        List<Usuario> usuarios = new ArrayList<>();
//        usuarios.add(new Usuario());
//        usuarios.add(new Usuario());
//
//        when(usuarioRepository.findByNomeContains(nome)).thenReturn(usuarios);
//
//        String result = adminController.filtroPorNome(nome, model, response);
//
//        assertEquals("listaUsuario", result);
//        verify(model, times(1)).addAttribute("usuarios", usuarios);
//        verify(response, times(1)).setStatus(HttpStatus.OK.value());
//    }
//}
//
