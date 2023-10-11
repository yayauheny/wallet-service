package yayauheny.service.command;

import yayauheny.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public void execute(Player player) {
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
    private void deletePlayer(List<Player> players) {
        System.out.println("Введите идентификатор пользователя для удаления:");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            long idFromConsole = Long.parseLong(reader.readLine());
            Optional<Player> maybePlayer = players.stream().filter(p -> p.getId().equals(idFromConsole)).findAny();

            if (maybePlayer.isPresent()) {
                Player player = maybePlayer.get();
                accountService.delete(player.getAccount());
                playerService.delete(maybePlayer.get());

                System.out.println("Пользователь успешно удален");
            } else {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            deletePlayer(players);
        }
    }
}

