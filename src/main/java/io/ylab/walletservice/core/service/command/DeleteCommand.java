package io.ylab.walletservice.core.service.command;

import io.ylab.walletservice.api.Auditor;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.exception.DatabaseException;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;


/**
 * The {@link DeleteCommand} class is an implementation of the {@link Command} interface,
 * allowing users to delete an existing player.
 */
public class DeleteCommand implements Command {

    /**
     * Executes the delete command to remove an existing player.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) throws DatabaseException {
        List<Player> players = printAllPlayers();
        deletePlayer(players);
    }

    /**
     * Gets the name of the delete command.
     *
     * @return The name of the command.
     */
    @Override
    public String getName() {
        return "Удаление игрока";
    }

    /**
     * Deletes a player based on user input.
     *
     * @param players The list of players to choose from.
     */
    private void deletePlayer(List<Player> players) throws DatabaseException {
        System.out.println("Введите идентификатор пользователя для удаления:");
        try {
            long idFromConsole = Long.parseLong(READER.readLine());
            Optional<Player> maybePlayer = players.stream().filter(p -> p.getId().equals(idFromConsole)).findAny();

            if (maybePlayer.isPresent()) {
                Player player = maybePlayer.get();
                accountService.delete(player.getAccount());
                playerService.delete(player);

                System.out.println("Пользователь успешно удален");
                Auditor.log("player: %s has been deleted" + player.getUsername());
            } else {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            deletePlayer(players);
        }
    }
}

