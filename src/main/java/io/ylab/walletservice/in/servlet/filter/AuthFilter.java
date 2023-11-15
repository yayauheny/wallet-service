package io.ylab.walletservice.in.servlet.filter;

import io.ylab.walletservice.api.JwtProvider;
import io.ylab.walletservice.util.ServletsUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(servletNames = {"PlayerServlet", "AccountServlet"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = ServletsUtils.getTokenFromRequest(httpRequest);
        if (isValidToken(token)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isValidToken(String token) {
        return token != null && JwtProvider.verifyToken(token);
    }
}
