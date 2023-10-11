package yayauheny.service.command;

import yayauheny.entity.Player;
import yayauheny.service.impl.Auditor;

import java.util.Queue;


/**
 * The {@link ReadLogsCommand} class is an implementation of the {@link Command} interface,
 * allowing users to read the logs of the current session.
 */
public class ReadLogsCommand implements Command {

    /**
     * Executes the command to read and display the logs of the current session.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) {
        Auditor.log("player: %s opened log".formatted(player.getUsername()));
        System.out.println("Список действий текущей сессии:");
        Queue<String> log = Auditor.getLog();
        log.forEach(System.out::println);
    }

    /**
     * Gets the name of the read logs command.
     *
     * @return The name of the command.
     */
    @Override
    public String getName() {
        return "Чтение логов текущей сессии";
    }
}

