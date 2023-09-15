package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.*;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.ecommerce.winery.models.CustomUserDetails;
import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.ImagemRepository;
import br.com.ecommerce.winery.repositories.ProdutoRepository;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Slf4j
public class PoderAdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ImagemRepository imagemRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final CPFValidator cpfValidator = new CPFValidator();

    public Usuario cadastrarUsuario(Usuario usuario) throws BusinessException {

        validarSenhasIguais(usuario.getSenha(), usuario.getConfirmaSenha());

        String senha = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senha);
        usuario.setConfirmaSenha(senha);
        validarEmailUnico(usuario.getEmail());
        validarCpf(usuario.getCpf());
        usuario.setStatus(Status.ATIVO);

        log.info("Usuário cadastrado com sucesso.");
        return usuarioRepository.save(usuario);
    }

    private boolean validarCpf(String cpf) throws BusinessException {
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

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario alternarStatusUsuario(int id) throws BusinessException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            if (usuario.getStatus().equals(Status.ATIVO)) {
                usuario.setStatus(Status.INATIVO);
                log.info("Usuário inativado com sucesso!");
            } else if (usuario.getStatus().equals(Status.INATIVO)) {
                usuario.setStatus(Status.ATIVO);
                log.info("Usuário reativado com sucesso!");
            }
            return usuarioRepository.save(usuario);
        }
        log.error("Usuário não encontrado!");
        throw new BusinessException("Usuário não encontrado!");
    }

    public Usuario alterarUsuario(Usuario usuarioAtualizado) throws BusinessException {
        Usuario usuarioCadastrado = usuarioRepository.findById(usuarioAtualizado.getId()).orElse(null);

        usuarioCadastrado.setNome(usuarioAtualizado.getNome());

        if (validarCpf(usuarioAtualizado.getCpf())) {
            usuarioCadastrado.setCpf(usuarioAtualizado.getCpf());
        }

        validarSenhasIguais(usuarioAtualizado.getSenha(), usuarioAtualizado.getConfirmaSenha());
        if (usuarioAtualizado.getSenha().isBlank() || usuarioAtualizado.getSenha().isEmpty()) {
            usuarioAtualizado.setSenha(usuarioCadastrado.getSenha());
            usuarioAtualizado.setConfirmaSenha(usuarioCadastrado.getConfirmaSenha());
        } else {

            String senha = passwordEncoder.encode(usuarioAtualizado.getSenha());
            usuarioAtualizado.setSenha(senha);
            usuarioAtualizado.setConfirmaSenha(senha);
            log.info("Senha alterada com sucesso!");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails usuarioLogado = (CustomUserDetails) authentication.getPrincipal();

            if (!usuarioLogado.getUsername().equals(usuarioCadastrado.getEmail())) {
                usuarioCadastrado.setGrupo(usuarioAtualizado.getGrupo());
                usuarioRepository.save(usuarioCadastrado);
            } else if (usuarioAtualizado.getGrupo().equals(usuarioCadastrado.getGrupo())) {
                log.info("nao atualizou o grupo. OK");
            } else {
                log.error("Acesso negado! Usuário não pode alterar o grupo do seu usuário");
                throw new BusinessException("Acesso negado! Não é possível alterar o grupo do seu usuário");
            }
        }

        return usuarioRepository.save(usuarioAtualizado);
    }

    public Usuario buscarUsuarioPorId(int id) throws BusinessException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        } else {
            throw new BusinessException("Usuário não encontrado com o ID: " + id);
        }
    }


    public Produto cadastrarProdutos(Produto produto,
                                     MultipartFile[] imagens,
                                     int imgPrincipal) throws BusinessException {

        try {
            produto.setNomeProduto(produto.getNomeProduto());
            produto.setDescricaoProduto(produto.getDescricaoProduto());
            produto.setPrecoProduto(produto.getPrecoProduto());
            produto.setQtdEstoque(produto.getQtdEstoque());
            produto.setStatusProduto(Status.ATIVO);

            log.info("Produto cadastrado com sucesso.");
            int p = 0;
            for (MultipartFile imagem : imagens) {
                if (imagem != null && !imagem.isEmpty()) {
                    try {
                        String imgFileName = salvaImagem(imagem, produto.getNomeProduto());
                        Imagem novaImagem = new Imagem();
                        if (imgPrincipal == p) {
                            novaImagem.setImagemPrincipal(true);
                        }
                        p++;
                        novaImagem.setUrl("imagens/produtos/" + imgFileName);
                        novaImagem.setProduto(produto);
                        imagemRepository.save(novaImagem);
                    } catch (Exception e) {
                        log.error("Falha ao armazenar uma imagem.", e);
                    }
                } else {
                    break;
                }
            }

            return produtoRepository.save(produto);

        } catch (Exception e) {
            log.error("Erro desconhecido ao cadastrar o produto.", e);
            throw new BusinessException("Erro ao cadastrar o produto.");
        }
    }

    private String salvaImagem(MultipartFile imagem, String produto) throws IOException {
        produto = produto.replaceAll("\\s+", "");
        String nomeArquivo = UUID.randomUUID() + "-" + produto;
        Path caminho = Paths.get("src/main/resources/static/imagensProdutos/" + nomeArquivo);
        Files.copy(imagem.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
        return nomeArquivo;
    }

    private void desmarcarOutrasImagens(Produto produto) {
        List<Imagem> imagemPrincipal = imagemRepository.findByImagemPrincipal(produto);
        for (Imagem imagem : imagemPrincipal) {
            imagem.setImagemPrincipal(false);
            imagemRepository.save(imagem);
        }
    }

    public Produto buscarProdutoPorId(int id) throws BusinessException {
        Produto produto = produtoRepository.findByIdProduto(id);

        if (produto != null) {
            return produto;
        } else {
            throw new BusinessException("Produto não encontrado com o ID: " + id);
        }
    }


    public Page<Produto> listarTodosProdutosDecrescente(Pageable pageable) {
        return produtoRepository.findAllByOrderByIdProdutoDesc(pageable);
    }

    public Produto ativarProduto(int idProduto) throws BusinessException {
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        if (produto != null) {
            if (produto.getStatusProduto().equals(Status.INATIVO)) {
                produto.setStatusProduto(Status.ATIVO);

                log.info("Produto ativado com sucesso!");
            }
            return produtoRepository.save(produto);
        }
        log.error("Produto não encontrado!");
        throw new BusinessException("Produto não encontrado!");
    }
    public Produto inativarProduto(int idProduto) throws BusinessException {
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        if (produto != null) {
            if (produto.getStatusProduto().equals(Status.ATIVO)) {
                produto.setStatusProduto(Status.INATIVO);

                log.info("Produto inativado com sucesso!");
            }
            return produtoRepository.save(produto);
        }
        log.error("Produto não encontrado!");
        throw new BusinessException("Produto não encontrado!");
    }


    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    public void alterarQuantidade(int idProduto, int novaQuantidade) throws BusinessException {

        Produto produto = produtoRepository.findById(idProduto).orElse(null);

        if(produto == null){
            log.error("Produto não encontrado!");
            throw new BusinessException("Produto não encontrado!");
        }
        if(novaQuantidade < 0){
            log.error("O estoque não pode ser menor do que 0!");
            throw new BusinessException("Estoque não pode ser negativo!");
        }

        produto.setQtdEstoque(novaQuantidade);
        produtoRepository.save(produto);
    }

    public Optional<Produto> findProdutoById(int id) {
        return produtoRepository.findById(id);
    }

    public Produto alteraProduto(int id, Produto produto) {
        Optional<Produto> optionalProduto = findProdutoById(id);

        if (optionalProduto.isPresent()) {
            Produto produtoExistente = optionalProduto.get();

            produtoExistente.setNomeProduto(produto.getNomeProduto());
            produtoExistente.setPrecoProduto(produto.getPrecoProduto());
            produtoExistente.setQtdEstoque(produto.getQtdEstoque());
            produtoExistente.setAvaliacaoProduto(produto.getAvaliacaoProduto());
            produtoExistente.setDescricaoProduto(produto.getDescricaoProduto());
            produtoExistente.setImagens(produto.getImagens());

            produtoRepository.save(produtoExistente);

            return produtoExistente;
        } else {
            throw new NoSuchElementException("Erro: Produto com ID " + id + " não encontrado.");
        }
    }

    public void editarProduto(Produto produto, MultipartFile[] imagemInput, String imagensParaRemover,
                              String imagensParaAtualizar, String imgPrincipal, RedirectAttributes redirect) {
        try {
            Optional<Produto> produtoOptional = produtoRepository.findById(produto.getIdProduto());

            if (!produtoOptional.isPresent()) {
                throw new BusinessException("Produto não encontrado!");
            } else {
                produtoRepository.save(produto);
            }
            int indiceImgPrincipal = -1;

            Boolean fotoPrinipalInImagens = false;
            if (imgPrincipal != null) {
                if ((!imgPrincipal.isEmpty() || !imgPrincipal.isBlank())
                        && (imagensParaAtualizar.isEmpty() || imagensParaAtualizar.isBlank())) {
                    fotoPrinipalInImagens = true;
                    try {
                        indiceImgPrincipal = Integer.parseInt(imgPrincipal);
                    } catch (NumberFormatException e) {
                        String msg=  e.getMessage();
                        throw new BusinessException(msg);
                    }

                }
            }

            if (imgPrincipal != null) {
                for (String id : imagensParaRemover.split(",")) {
                    if (!id.isBlank() || !id.isEmpty()) {
                        System.out.println("id para remover :" + id);
                        var imagemDell = imagemRepository.findById(Integer.parseInt(id));
                        if (imagemDell.isPresent()) {
                            System.out.println(imagemDell.get());
                            var img = imagemDell.get();
                            imagemRepository.deleteById(img.getImagem_id());
                            removeImagem(img.getUrl());

                        }
                    }
                }
            }

            if (imgPrincipal != null) {
                if (!fotoPrinipalInImagens) {
                    var imgList = imagemRepository.findAllByProduto(produto);
                    var IdImgUpdate = Long.parseLong(imagensParaAtualizar);
                    System.out.println("IdImgUpdate: " + IdImgUpdate);
                    for (Imagem img : imgList) {
                        if (img.getImagem_id() == IdImgUpdate) {
                            img.setImagemPrincipal(true);
                            imagemRepository.save(img);
                        } else {
                            img.setImagemPrincipal(false);
                            imagemRepository.save(img);
                        }
                    }
                } else {
                    var imgList = imagemRepository.findAllByProduto(produto);
                    for (Imagem img : imgList) {
                        if (img.isImagemPrincipal()) {
                            img.setImagemPrincipal(false);
                            imagemRepository.save(img);
                        }
                    }
                }
            }

            if (imagemInput != null) {
                MultipartFile[] imagens = (MultipartFile[]) imagemInput;
                int p = 0;
                for (MultipartFile img : imagens) {
                    if (img != null && !img.isEmpty()) {
                        try {
                            String imgFileName = salvaImagem(img, produto.getNomeProduto());
                            Imagem novaImagem = new Imagem();

                            if (indiceImgPrincipal == p && fotoPrinipalInImagens) {
                                novaImagem.setImagemPrincipal(true);
                            } else {
                                novaImagem.setImagemPrincipal(false);
                            }
                            p++;
                            novaImagem.setUrl("imagensProdutos/" + imgFileName);
                            novaImagem.setProduto(produto);
                            imagemRepository.save(novaImagem);
                        } catch (Exception e) {
                            String nomeImg = img.getOriginalFilename();
                            System.out.println("Falha ao armazenar a imagem " + nomeImg + e);
                            String nomeImagem2 = "default.jpg";
                            String caminho2 = "imagensProdutos/" + nomeImagem2;
                            Imagem novaImagem = new Imagem();
                            novaImagem.setUrl(caminho2);
                            novaImagem.setProduto(produto);
                            novaImagem.setImagemPrincipal(true);
                            imagemRepository.save(novaImagem);
                            break;
                        }

                    } else {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());

        }
    }

    private void removeImagem(String nomeArquivo) {
        Path caminho = Paths.get("src/main/resources/static/" + nomeArquivo);
        try {
            Files.delete(caminho);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

