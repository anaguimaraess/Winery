package br.com.ecommerce.winery.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            if (Objects.equals(savedRequest.getRedirectUrl(), "/verificar-sessao") || Objects.equals(savedRequest.getRedirectUrl(), "http://localhost:8082/verificar-sessao")) {
                response.sendRedirect("/carrinho");
            } else {
                response.sendRedirect(savedRequest.getRedirectUrl());
            }
        } else {
            String redirectURL = determineTargetUrl(authentication);
            response.sendRedirect(redirectURL);
        }
    }

    protected String determineTargetUrl(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .collect(Collectors.toSet());

        if (roles.contains("ADMIN")) {
            return "/admin/home";
        } else if (roles.contains("ESTOQUISTA")) {
            return "/estoque/homeEstoque";
        } else if (roles.contains("CLIENTE")) {
            return "/Winery";
        } else {
            return "/Winery";  // para outros usuários ou default
        }
    }
}