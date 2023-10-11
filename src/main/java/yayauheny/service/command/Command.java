
package yayauheny.service.command;

import yayauheny.entity.Player;
import yayauheny.service.impl.AccountServiceImpl;
import yayauheny.service.impl.PlayerServiceImpl;

import java.util.List;

/**
 * The {@link Command} interface is part of the command pattern,
 * defining the contract for command objects that can be executed
 * based on user permissions.
 */
public interface Command {

    /**
     * The singleton instance of the {@code PlayerServiceImpl}.
     */
    PlayerServiceImpl playerService = PlayerServiceImpl.getInstance();

    /**
     * The singleton instance of the {@code AccountServiceImpl}.
     */
    AccountServiceImpl accountService = AccountServiceImpl.getInstance();

    /**
     * The format for displaying player information.
     */
    String PLAYER_FORMAT = """
            Идентификатор: %s
            Имя: %s
            Дата рождения: %s
            Роль: %s\n
            """;

    /**
     * Executes the command for the specified player.
     *
     * @param player The player on which the command is executed.
     */
    void execute(Player player);

    /**
     * Gets the name of the command.
     *
     * @return The name of the command.
     */
    String getName();

    /**
     * Prints the information of all players.
     *
     * @return The list of all players.
     */
    default List<Player> printAllPlayers() {
        System.out.println("Список пользователей:");
        List<Player> players = playerService.findAll();

        for (Player player : players) {
            System.out.println(String.format(PLAYER_FORMAT,
                    player.getId(),
                    player.getUsername(),
                    player.getBirthDate(),
                    player.getRole().name()));
        }
        return players;
    }
}
