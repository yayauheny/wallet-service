package io.ylab.walletservice.in.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.ylab.walletservice.api.JwtProvider;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.dto.account.AccountGetDto;
import io.ylab.walletservice.core.dto.account.AccountResponse;
import io.ylab.walletservice.core.dto.player.PlayerCreateDto;
import io.ylab.walletservice.core.dto.player.PlayerUpdateDto;
import io.ylab.walletservice.core.dto.player.PlayerResponse;
import io.ylab.walletservice.core.service.impl.AccountServiceImpl;
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
import java.util.stream.Collectors;

@WebServlet("/accounts")
public class AccountServlet extends HttpServlet {

    private AccountServiceImpl accountService;

    @Override
    public void init() throws ServletException {
        this.accountService = new AccountServiceImpl();
        super.init();
    }

    //TODO: VALIDATOR
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            String token = ServletsUtils.getTokenFromRequest(req);
            String idFromReq = req.getParameter("id");
            String playerIdFromReq = req.getParameter("player_id");
            AccountGetDto accountDto = new AccountGetDto(idFromReq, playerIdFromReq, token);

            if (idFromReq == null && playerIdFromReq == null) {
                processFindAllRequest(resp, writer, token);
            } else if (playerIdFromReq != null) {
                processGetByPlayerIdRequest(resp, writer, accountDto);
            } else {
                processGetByIdRequest();
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

    private void processGetByPlayerIdRequest(HttpServletResponse resp, PrintWriter writer, AccountGetDto accountDto) throws IOException {
        try {
            Long payloadPlayerId = JwtProvider.getPlayerIdFromToken(accountDto.token());
            Long playerId = Long.valueOf(accountDto.playerId());

            if (payloadPlayerId.equals(playerId)) {
                Optional<Account> account = accountService.findByPlayerId(playerId);
                if(account.isPresent()){

                }
                AccountResponse response = accountService.findByPlayerId(playerId);
                ServletsUtils.setOkAndFlush(resp, writer, JsonParser.toJson(response));
            } else {
                ServletsUtils.processRequestForDisallowedAction(resp, writer);
            }
        } catch (NumberFormatException e) {
            ServletsUtils.setBadRequestAndFlush(resp, writer);
        }
    }

    private void processFindAllRequest(HttpServletResponse resp, PrintWriter writer, String token) throws IOException {
        if (ServletsUtils.hasAdminRole(token)) {
            List<AccountResponse> accountsResponseList = accountService.findAll();
            ServletsUtils.setOkAndFlush(resp, writer, JsonParser.toJson(accountsResponseList));
        } else {
            ServletsUtils.processRequestForDisallowedRole(resp, writer);
        }
    }

    private void processPostRequest(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer, PlayerCreateDto playerRequest) {
        try {
            PlayerResponse playerResponse = playerService.save(playerRequest);
            String resourceLocation = String.format("%s/%s", req.getRequestURL(), playerResponse.id());
            ServletsUtils.setCreatedAndFlush(resp, writer, JsonParser.toJson(playerResponse), resourceLocation);
        } catch (DatabaseException | JsonProcessingException e) {
            ServletsUtils.setBadRequestAndFlush(resp, writer);
        }
    }

    private void processPutRequest(HttpServletResponse resp, PrintWriter writer, PlayerUpdateDto playerRequest) throws JsonProcessingException {
        try {
            playerService.update(playerRequest);
            ServletsUtils.setOkAndFlush(resp, writer, JsonParser.toJson(playerRequest));
        } catch (DatabaseException e) {
            ServletsUtils.setBadRequestAndFlush(resp, writer);
        }
    }
}
