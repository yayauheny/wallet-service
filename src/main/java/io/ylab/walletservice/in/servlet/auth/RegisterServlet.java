package io.ylab.walletservice.in.servlet.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.ylab.walletservice.api.JwtProvider;
import io.ylab.walletservice.core.domain.PlayerRole;
import io.ylab.walletservice.core.dto.player.PlayerCreateDto;
import io.ylab.walletservice.core.dto.player.PlayerResponse;
import io.ylab.walletservice.core.service.impl.PlayerServiceImpl;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.exception.NotFoundException;
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
import java.util.stream.Collectors;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private PlayerServiceImpl playerService;

    @Override
    public void init() throws ServletException {
        playerService = new PlayerServiceImpl();
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (BufferedReader reader = req.getReader();
             PrintWriter writer = resp.getWriter()) {
            String requestBody = reader.lines().collect(Collectors.joining());
            PlayerCreateDto playerRequest = JsonParser.fromJson(requestBody, PlayerCreateDto.class);

            if (checkIfExistsByUsername(playerRequest.username())) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                writer.print("User with the provided username already exists.");
                writer.flush();
                return;
            }
            if (playerRequest.role().equals(PlayerRole.ADMIN)) {
                ServletsUtils.processRequestForDisallowedRole(resp, writer);
                return;
            }

            try {
                PlayerResponse player = playerService.save(playerRequest);
                String jwtToken = JwtProvider.generateTokenForPlayer(player);
                ServletsUtils.setJwtTokenToResponse(jwtToken, resp);
            } catch (JsonProcessingException | IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.print("Invalid data, some of the parameters are incorrect");
            } catch (DatabaseException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.print("Error performing database, try again later");
            }
        }
    }

    private boolean checkIfExistsByUsername(String username) {
        try {
            playerService.findByUsername(username);
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }
}
