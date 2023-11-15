package io.ylab.walletservice.in.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.ylab.walletservice.api.JwtProvider;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.dto.player.PlayerCreateDto;
import io.ylab.walletservice.core.dto.player.PlayerGetDto;
import io.ylab.walletservice.core.dto.player.PlayerUpdateDto;
import io.ylab.walletservice.core.dto.player.PlayerResponse;
import io.ylab.walletservice.core.mapper.PlayerMapper;
import io.ylab.walletservice.core.service.impl.PlayerServiceImpl;
import io.ylab.walletservice.exception.DatabaseException;
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
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@WebServlet("/players")
public class PlayerServlet extends HttpServlet {

    private PlayerServiceImpl playerService;

    @Override
    public void init() throws ServletException {
        this.playerService = new PlayerServiceImpl();
        super.init();
    }

    //TODO: VALIDATOR
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            String token = ServletsUtils.getTokenFromRequest(req);
            String idFromReq = req.getParameter("id");
            String usernameFromReq = req.getParameter("username");
            PlayerGetDto playerGetDto = new PlayerGetDto(idFromReq, usernameFromReq, token);

            if (idFromReq == null && usernameFromReq == null) {
                processFindAllRequest(resp, writer, token);
            } else if (usernameFromReq != null) {
                processGetByUsernameRequest(resp, writer, playerGetDto);
            } else {
                processGetByIdRequest(resp, writer, playerGetDto);
            }
        }
    }

    //TODO: VALIDATOR
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (BufferedReader reader = req.getReader();
             PrintWriter writer = resp.getWriter()) {
            String token = ServletsUtils.getTokenFromRequest(req);
            String requestBody = reader.lines().collect(Collectors.joining());
            //validate
            PlayerCreateDto playerRequest = JsonParser.fromJson(requestBody, PlayerCreateDto.class);
            if (ServletsUtils.hasAdminRole(token)) {
                processPostRequest(req, resp, writer, playerRequest);
            } else {
                ServletsUtils.processRequestForDisallowedRole(resp, writer);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (BufferedReader reader = req.getReader();
             PrintWriter writer = resp.getWriter()) {
            String token = ServletsUtils.getTokenFromRequest(req);
            String requestBody = reader.lines().collect(Collectors.joining());
            //validate
            PlayerUpdateDto playerRequest = JsonParser.fromJson(requestBody, PlayerUpdateDto.class);

            if (ServletsUtils.hasAdminRole(token)) {
                processPutRequest(resp, writer, playerRequest);
            } else {
                Long payloadId = JwtProvider.getPlayerIdFromToken(token);
                if (payloadId.equals(playerRequest.id())) {
                    processPutRequest(resp, writer, playerRequest);
                } else {
                    ServletsUtils.processRequestForDisallowedAction(resp, writer);
                }
            }
        }
    }

    //TODO: validator
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            String token = ServletsUtils.getTokenFromRequest(req);
            String idFromReq = req.getParameter("id");
            //validate

            if (idFromReq == null) {
                ServletsUtils.setBadRequestAndFlush(resp, writer);
            } else {
                Long id = Long.valueOf(idFromReq);
                playerService.delete(id);
                ServletsUtils.setNoContentAndFlush(resp, writer);
            }
        }
    }

    private void processGetByIdRequest(HttpServletResponse resp, PrintWriter writer, PlayerGetDto playerDto) throws IOException {
        Long id = Long.valueOf(playerDto.id());
        String token = playerDto.token();
        Long payloadId = JwtProvider.getPlayerIdFromToken(playerDto.token());

        if (ServletsUtils.hasAdminRole(token)) {
            handleFindPlayerByIdRequest(resp, writer, id);
        } else if (payloadId.equals(id)) {
            handleFindPlayerByIdRequest(resp, writer, id);
        } else {
            ServletsUtils.processRequestForDisallowedAction(resp, writer);
        }
    }

    private void handleFindPlayerByIdRequest(HttpServletResponse resp, PrintWriter writer, Long id) {
        Optional<Player> player = playerService.findById(id);
        player.ifPresentOrElse(
                p -> {
                    PlayerResponse response = PlayerMapper.INSTANCE.toResponse(p);
                    try {
                        ServletsUtils.setOkAndFlush(resp, writer, JsonParser.toJson(response));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> ServletsUtils.setBadRequestAndFlush(resp, writer)
        );
    }

    private void processGetByUsernameRequest(HttpServletResponse resp, PrintWriter writer, PlayerGetDto playerDto) throws
            IOException {
        String payloadUsername = JwtProvider.getPlayerUsernameFromToken(playerDto.token());

        if (payloadUsername.equals(playerDto.username())) {
            PlayerResponse playerResponse = playerService.findByUsername(playerDto.username());
            ServletsUtils.setOkAndFlush(resp, writer, JsonParser.toJson(playerResponse));
        } else {
            ServletsUtils.processRequestForDisallowedAction(resp, writer);
        }
    }

    private void processFindAllRequest(HttpServletResponse resp, PrintWriter writer, String token) throws
            IOException {
        if (ServletsUtils.hasAdminRole(token)) {
            List<PlayerResponse> playersResponseList = playerService.findAll();
            ServletsUtils.setOkAndFlush(resp, writer, JsonParser.toJson(playersResponseList));
        } else {
            ServletsUtils.processRequestForDisallowedRole(resp, writer);
        }
    }

    private void processPostRequest(HttpServletRequest req, HttpServletResponse resp, PrintWriter
            writer, PlayerCreateDto playerRequest) {
        try {
            PlayerResponse playerResponse = playerService.save(playerRequest);
            String resourceLocation = String.format("%s/%s", req.getRequestURL(), playerResponse.id());
            ServletsUtils.setCreatedAndFlush(resp, writer, JsonParser.toJson(playerResponse), resourceLocation);
        } catch (DatabaseException | JsonProcessingException e) {
            ServletsUtils.setBadRequestAndFlush(resp, writer);
        }
    }

    private void processPutRequest(HttpServletResponse resp, PrintWriter writer, PlayerUpdateDto playerRequest) throws
            JsonProcessingException {
        try {
            playerService.update(playerRequest);
            ServletsUtils.setOkAndFlush(resp, writer, JsonParser.toJson(playerRequest));
        } catch (DatabaseException e) {
            ServletsUtils.setBadRequestAndFlush(resp, writer);
        }
    }
}
