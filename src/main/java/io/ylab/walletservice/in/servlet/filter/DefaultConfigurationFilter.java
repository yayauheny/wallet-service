package io.ylab.walletservice.in.servlet.filter;

import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.infrastructure.database.ConnectionManager;
import io.ylab.walletservice.infrastructure.database.LiquibaseMigration;
import io.ylab.walletservice.util.PropertiesUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter("/*")
public class DefaultConfigurationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.setProperty("app.environment", "dev");
        ConnectionManager.reloadConfiguration();
        try (Connection connection = ConnectionManager.getConnection()) {
            LiquibaseMigration.update(PropertiesUtil.get("db.migrations.changelog-file"), connection);
        } catch (DatabaseException | SQLException e) {
            throw new RuntimeException(e);
        }
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        chain.doFilter(request, response);
    }
}
