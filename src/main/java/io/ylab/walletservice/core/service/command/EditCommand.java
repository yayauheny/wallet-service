package io.ylab.walletservice.core.service.command;

import io.ylab.walletservice.api.Auditor;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.PlayerRole;
import io.ylab.walletservice.util.DateTimeUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;


/**
 * The {@link EditCommand} class is an implementation of the {@link Command} interface,
 * allowing users to edit an existing player's information.
 */
public class EditCommand implements Command {

    /**
     * Executes the edit command to modify an existing player's details.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) {
        List<Player> players = printAllPlayers();
        readIdFromConsole(players);
    }

    /**
     * Gets the name of the edit command.
     *
     * @return The name of the command.
     */
    @Override
    public String getName() {
        return "Изменение игрока";
    }

    /**
     * Reads the user input for the player's ID to be edited.
     *
     * @param players The list of players to choose from.
     */
    private void readIdFromConsole(List<Player> players) {
        System.out.println("Введите идентификатор пользователя для изменения:");
        try {
            long idFromConsole = Long.parseLong(READER.readLine());
            Optional<Player> maybePlayer = players.stream().filter(p -> p.getId().equals(idFromConsole)).findAny();

            if (maybePlayer.isPresent()) {
                updatePlayer(maybePlayer.get());
            } else {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            readIdFromConsole(players);
        }
    }

    /**
     * Updates the player's information based on user input.
     *
     * @param player The player to be updated.
     */
    private void updatePlayer(Player player) {
        System.out.println("Изменить поля:\n1 - имя\n2 - дата рождения\n3 - роль");
        try {
            byte choice = Byte.parseByte(READER.readLine());
            switch (choice) {
                case 1 -> {
                    System.out.println("Введите новое имя:");
                    String inputName = READER.readLine();
                    player.setUsername(inputName);
                    playerService.update(player);
                }
                case 2 -> {
                    System.out.println("Введите новую дату рождения (гггг.мм.дд):");
                    String inputDate = READER.readLine();
                    LocalDate date = LocalDate.parse(inputDate, DateTimeUtils.dateFormatter);
                    player.setBirthDate(date);
                    playerService.update(player);
                }
                case 3 -> {
                    System.out.println("Выберите роль игрока:\n1 - пользователь\n2 - администратор");
                    byte inputRole = Byte.parseByte(READER.readLine());
                    switch (inputRole) {
                        case 1 -> {
                            player.setRole(PlayerRole.USER);
                            playerService.update(player);
                        }
                    }
                }
            }
            System.out.println("Данные изменены");
            Auditor.log("player: %s info have been changed".formatted(player.getUsername()));
        } catch (InputMismatchException | DateTimeParseException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            updatePlayer(player);
        }
    }
}

