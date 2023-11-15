package io.ylab.walletservice.in.servlet.auth;

import com.fasterxml.jackson.databind.JsonNode;
import io.ylab.walletservice.api.JwtProvider;
import io.ylab.walletservice.api.Validator;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.dto.player.PlayerResponse;
import io.ylab.walletservice.core.mapper.PlayerMapper;
import io.ylab.walletservice.core.service.impl.PlayerServiceImpl;
import io.ylab.walletservice.exception.ValidationException;
import io.ylab.walletservice.util.JsonParser;
import io.ylab.walletservice.util.ServletsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {

    private PlayerServiceImpl playerService;

    @Override
    public void init() throws ServletException {
        this.playerService = new PlayerServiceImpl();
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (BufferedReader reader = req.getReader();
             PrintWriter writer = resp.getWriter()) {
            String requestBody = reader.lines().collect(Collectors.joining());
            JsonNode jsonNode = JsonParser.toNode(requestBody);
            String username = JsonParser.getValue(jsonNode, "username");
            String password = JsonParser.getValue(jsonNode, "password");

            try {
                Validator.validateForNull(username, password);
                if (!playerService.verifyPassword(password, username)) {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    Optional<Player> player = playerService.findByUsername(username);
                    player.ifPresentOrElse(
                            p -> {
                                PlayerResponse response = PlayerMapper.INSTANCE.toResponse(p);
                                String jwtToken = JwtProvider.generateTokenForPlayer(response);
                                try {
                                    ServletsUtils.setJwtTokenToResponse(jwtToken, resp);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            () -> ServletsUtils.setBadRequestAndFlush(resp, writer)
                    );
                }
            } catch (ValidationException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.print("Invalid data, some of the parameters are null");
                writer.flush();
            }
        }
    }
}
