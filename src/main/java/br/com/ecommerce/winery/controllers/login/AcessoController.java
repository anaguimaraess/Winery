//package br.com.ecommerce.winery.controllers.login;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@Controller
//@RequestMapping("")
//public class AcessoController {
//
//    @GetMapping("admin/cadastrar")
//    @ResponseStatus(HttpStatus.OK)
//    public String admin(@RequestParam(required = false) String nomeBusca, Model model) {
//        return "cadastrUsuario";
//    }
//
//    @GetMapping("/estoque/dashboard")
//    @ResponseStatus(HttpStatus.OK)
//    public String estoque(@RequestParam(required = false) String nomeBusca, Model model) {
//        return "estoque/index";
//    }
//
//    @GetMapping("/winery")
//    @ResponseStatus(HttpStatus.OK)
//    public String home(@RequestParam(required = false) String nomeBusca, Model model) {
//        return "index";
//    }
//
//}