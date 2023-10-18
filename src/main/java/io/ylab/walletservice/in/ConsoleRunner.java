package io.ylab.walletservice.in;

import io.ylab.walletservice.api.Auditor;
import io.ylab.walletservice.api.PasswordHasher;
import io.ylab.walletservice.api.PermissionsManager;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.service.command.Command;
import io.ylab.walletservice.core.service.command.CreateCommand;
import io.ylab.walletservice.core.service.impl.PlayerServiceImpl;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.infrastructure.database.ConnectionManager;
import io.ylab.walletservice.infrastructure.database.LiquibaseMigration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

/**
 * The {@link ConsoleRunner} class serves as the entry point for the console-based user interface.
 * It allows users to authenticate, register, and execute various commands based on their roles.
 */
public class ConsoleRunner {

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private static final String INCORRECT_INPUT = "Некорректный ввод, попробуйте снова";
    private static PlayerServiceImpl playerService = new PlayerServiceImpl();
    private static Player currentPlayer;

    static {
        System.setProperty("app.environment", "dev");
        ConnectionManager.reloadConfiguration();
        LiquibaseMigration.update();
    }

    /**
     * The main method to start the console-based user interface.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        authenticate();
        try {
            READER.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Authenticates the user by providing login or registration options.
     *
     * @return The authenticated player.
     */
    private static void authenticate() {
        String authMessage = """
                Войдите или зарегистрируйтесь в системе, чтобы продолжить работу:
                1 - войти
                2 - зарегистрироваться
                3 - выйти
                """;
        try {
            System.out.println(authMessage);
            int choice = Integer.parseInt(READER.readLine());

            switch (choice) {
                case 1 -> {
                    if (login()) {
                        authorize(currentPlayer);
                    }
                }
                case 2 -> {
                    register();
                    authorize(currentPlayer);
                }
                case 3 -> {
                    System.out.println("Выход из приложения");
                    ConnectionManager.closeConnectionPool();
                }
                default -> throw new InputMismatchException();
            }
        } catch (InputMismatchException | NumberFormatException | IOException e) {
            System.err.println(INCORRECT_INPUT);
            authenticate();
        } catch (DatabaseException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Authorizes the user and allows the execution of various commands based on their role.
     *
     * @param player The authenticated player.
     */
    private static void authorize(Player player) throws DatabaseException {
        if (currentPlayer == null) {
            authenticate();
        }

        Auditor.log("player: %s have been authorized".formatted(currentPlayer.getUsername()));
        System.out.println("\nСписок доступных действий:");
        List<Command> commands = PermissionsManager.getCommands(currentPlayer.getRole());
        for (int i = 0; i < commands.size(); i++) {
            System.out.println(i + 1 + " - " + commands.get(i).getName());
        }
        int exitNumber = commands.size() + 1;
        int exitFromAccountNumber = commands.size() + 2;
        System.out.println(exitNumber + " - выйти\n" + exitFromAccountNumber + " - выйти из аккаунта");

        System.out.println("Введите команду:");
        try {
            int choice = Integer.parseInt(READER.readLine());
            if (choice == exitNumber) {
                System.out.println("Завершение работы...");
            } else if (choice == exitFromAccountNumber) {
                currentPlayer = null;
                ConnectionManager.closeConnectionPool();
                authenticate();
            } else if (choice > 0 && choice <= commands.size()) {
                commands.get(choice - 1).execute(player);
                authorize(player);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.err.println(INCORRECT_INPUT);
            authorize(currentPlayer);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Logs in an existing player by providing a username and password.
     *
     * @return The logged-in player.
     */
    private static boolean login() throws DatabaseException {
        try {
            System.out.println("Введите логин:");
            String loginInput = READER.readLine();
            System.out.println("Введите пароль:");
            String password = READER.readLine();
            return processLoginCredentials(loginInput, password);
        } catch (InputMismatchException | IOException e) {
            System.err.println(INCORRECT_INPUT);
            return login();
        }
    }

    /**
     * Registers a new player by providing user details.
     *
     * @return The newly registered player.
     */
    private static void register() throws DatabaseException {
        Command command = new CreateCommand();
        command.execute(null);
    }

    /**
     * Processes the login credentials provided by the user.
     *
     * @param username      The entered username.
     * @param inputPassword The entered password.
     * @return The authenticated player.
     */
    private static boolean processLoginCredentials(String username, String inputPassword) throws DatabaseException {
        Optional<Player> maybePlayer = playerService.findByUsername(username);
        boolean isVerified = false;

        if (maybePlayer.isPresent()) {
            Player player = maybePlayer.get();
            byte[] originalPassword = player.getHashedPassword();

            if (PasswordHasher.checkPassword(inputPassword, originalPassword)) {
                currentPlayer = player;
                isVerified = true;
                Auditor.log("player: %s is logged in".formatted(player.getUsername()));
            } else {
                System.err.println(INCORRECT_INPUT);
            }
        } else {
            System.err.println("Такого пользователя не существует, зарегистрируйтесь или проверьте логин");
            authenticate();
        }
        return isVerified;
    }
}

