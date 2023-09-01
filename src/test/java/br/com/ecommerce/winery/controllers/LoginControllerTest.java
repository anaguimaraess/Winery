package br.com.ecommerce.winery.controllers;

import br.com.ecommerce.winery.controllers.login.LoginController;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Controller
@RequestMapping(path = "/authentication")
public class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;

    @Mock
    private UsuarioRepository usuarioRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html"); //
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testGetLoginForm() throws Exception {
        mockMvc.perform(get("/authentication/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
