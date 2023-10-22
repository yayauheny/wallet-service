package io.ylab.walletservice.core.service.command;

import io.ylab.walletservice.api.Auditor;
import io.ylab.walletservice.api.PasswordHasher;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.PlayerRole;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.util.DateTimeUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;


/**
 * The {@link CreateCommand} class is an implementation of the {@link Command} interface,
 * allowing users to register a new player.
 */
public class CreateCommand implements Command {

    /**
     * Executes the create command to register a new player.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) throws DatabaseException {
        System.out.println("Регистрация нового игрока:");
        System.out.println("Введите имя:");
        try {
            String inputName = READER.readLine();
            System.out.println("Введите дату рождения (гггг.мм.дд)");
            String inputDate = READER.readLine();
            LocalDate birthDate = LocalDate.parse(inputDate, DateTimeUtils.dateFormatter);
            System.out.println("Выберите роль игрока:\n1 - пользователь\n2 - администратор");
            int inputRole = Integer.parseInt(READER.readLine());
            PlayerRole role;

            switch (inputRole) {
                case 1 -> role = PlayerRole.USER;
                case 2 -> role = PlayerRole.ADMIN;
                default -> throw new InputMismatchException();
            }

            System.out.println("Введите пароль:");
            String inputPassword = READER.readLine();
            Player createdPlayer = registerPlayer(inputName, role, PasswordHasher.hashPassword(inputPassword), birthDate);
            System.out.println("Аккаунт был успешно зарегистрирован.");
            Auditor.log("player: %s has been created".formatted(createdPlayer.getUsername()));
        } catch (InputMismatchException | DateTimeParseException | NumberFormatException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            execute(player);
        }
    }

    /**
     * Gets the name of the create command.
     *
     * @return The name of the command.
     */
    @Override
    public String getName() {
        return "Регистрация нового игрока";
    }

    /**
     * Registers a new player with the provided details.
     *
     * @param username  The username of the player.
     * @param role      The role of the player.
     * @param password  The hashed password of the player.
     * @param birthDate The birthdate of the player.
     * @return The registered player.
     */
    private Player registerPlayer(String username, PlayerRole role, byte[] password, LocalDate birthDate) throws DatabaseException {
        Player player = Player.builder()
                .username(username)
                .role(role)
                .hashedPassword(password)
                .birthDate(birthDate)
                .build();
        Account playerAccount = Account.builder()
                .playerId(player.getId())
                .currencyCode(DEFAULT_CURRENCY.getCode())
                .build();
        player.setAccount(playerAccount);
        playerService.save(player);
        accountService.save(playerAccount);
        return player;
    }
}

