package yayauheny.utils;

import lombok.experimental.UtilityClass;
import yayauheny.entity.PlayerRole;
import yayauheny.service.command.BalanceOperationsCommand;
import yayauheny.service.command.Command;
import yayauheny.service.command.CreateCommand;
import yayauheny.service.command.DeleteCommand;
import yayauheny.service.command.EditCommand;
import yayauheny.service.command.ReadLogsCommand;
import yayauheny.service.command.ReadTransactionsCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The {@link PermissionsManager} utility class manages the permissions for each player role
 * by associating roles with a list of permitted commands.
 */
@UtilityClass
public class PermissionsManager {

    /**
     * A mapping of player roles to the list of permitted commands.
     */
    public static final Map<PlayerRole, List<Command>> permissions = Map.ofEntries(
            Map.entry(PlayerRole.ADMIN, Arrays.asList(
                    new CreateCommand(),
                    new DeleteCommand(),
                    new BalanceOperationsCommand(),
                    new EditCommand(),
                    new ReadLogsCommand(),
                    new ReadTransactionsCommand())),
            Map.entry(PlayerRole.USER, Arrays.asList(
                    new BalanceOperationsCommand(),
                    new CreateCommand()))
    );

    /**
     * Gets the list of permitted commands for a given player role.
     *
     * @param role The player role.
     * @return The list of permitted commands for the specified role.
     */
    public List<Command> getCommands(PlayerRole role) {
        return permissions.get(role);
    }
}

