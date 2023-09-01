package br.com.ecommerce.winery.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "")
public class HomeController {
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHomeForm() {
        return "home";
    }

}
