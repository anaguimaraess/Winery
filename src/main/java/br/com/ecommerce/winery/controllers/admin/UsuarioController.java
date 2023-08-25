package br.com.ecommerce.winery.controllers.admin;
import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.admin.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;





}
