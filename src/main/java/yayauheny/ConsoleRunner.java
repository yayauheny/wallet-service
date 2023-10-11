package yayauheny;

import yayauheny.entity.Account;
import yayauheny.entity.Currency;
import yayauheny.entity.Player;
import yayauheny.entity.PlayerRole;
import yayauheny.service.command.Command;
import yayauheny.service.command.CreateCommand;
import yayauheny.service.impl.AccountServiceImpl;
import yayauheny.service.impl.PlayerServiceImpl;
import yayauheny.utils.PasswordHasher;
import yayauheny.utils.PermissionsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;


/**
 * The {@link ConsoleRunner} class serves as the entry point for the console-based user interface.
 * It allows users to authenticate, register, and execute various commands based on their roles.
 */
public class ConsoleRunner {

    private static final PlayerServiceImpl playerService = PlayerServiceImpl.getInstance();
    private static final AccountServiceImpl accountService = AccountServiceImpl.getInstance();
    private static final Currency USD = new Currency(BigDecimal.ONE, "USD");
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final String INCORRECT_INPUT = "Некорректный ввод, попробуйте снова";
    private static Player currentPlayer;

    /**
     * The main method to start the console-based user interface.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) throws IOException {
        authenticate();
        reader.close();
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
            int choice = Integer.parseInt(reader.readLine());
            switch (choice) {
                case 1 -> {
                    if (login()) {
                        authorize(currentPlayer);
                    }
                }
                case 2 -> {
                    if (register()) {
                        authorize(currentPlayer);
                    }
                }
                case 3 -> System.out.println("Выход из приложения");
                default -> throw new InputMismatchException();
            }
        } catch (InputMismatchException | NumberFormatException |
                 IOException e) {
            System.err.println(INCORRECT_INPUT);
            authenticate();
        }
    }

    /**
     * Authorizes the user and allows the execution of various commands based on their role.
     *
     * @param player The authenticated player.
     */
    private static void authorize(Player player) {
        if (currentPlayer == null) {
            authenticate();
        }

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
            int choice = Integer.parseInt(reader.readLine());

            if (choice == exitNumber) {
                System.out.println("Завершение работы...");
            } else if (choice == exitFromAccountNumber) {
                currentPlayer = null;
                authenticate();
            } else if (choice > 0 && choice <= commands.size()) {
                commands.get(choice - 1).execute(player);
                authorize(player);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(INCORRECT_INPUT);
            authorize(currentPlayer);
        }
    }

    /**
     * Logs in an existing player by providing a username and password.
     *
     * @return The logged-in player.
     */
    private static boolean login() {
        try {
            System.out.println("Введите логин:");
            String loginInput = reader.readLine();
            System.out.println("Введите пароль:");
            String password = reader.readLine();
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
    private static boolean register() {
        return registerNewPlayer();
    }

    /**
     * Processes the login credentials provided by the user.
     *
     * @param username      The entered username.
     * @param inputPassword The entered password.
     * @return The authenticated player.
     */
    private static boolean processLoginCredentials(String username, String inputPassword) {
        Optional<Player> maybePlayer = playerService.findByUsername(username);
        boolean isVerified = false;
        if (maybePlayer.isPresent()) {
            Player player = maybePlayer.get();
            byte[] originalPassword = player.getHashedPassword();
            if (PasswordHasher.checkPassword(inputPassword, originalPassword)) {
                currentPlayer = player;
                isVerified = true;
            } else {
                System.err.println(INCORRECT_INPUT);
            }
        } else {
            System.err.println("Такого пользователя не существует, зарегистрируйтесь или проверьте логин");
        }
        return isVerified;
    }

    /**
     * Registers a new player by providing user details.
     *
     * @return The newly registered player.
     */
    private static boolean registerNewPlayer() {
        Command command = new CreateCommand();
        command.execute(null);
        return true;
    }

    /**
     * Creates a new player and associated account based on user details.
     *
     * @param username  The player's username.
     * @param role      The player's role.
     * @param password  The hashed password.
     * @param birthDate The player's birth date.
     * @return The newly created player.
     */
    private static Player createNewPlayer(String username, PlayerRole role, byte[] password, LocalDate birthDate) {
        Player player = Player.builder()
                .username(username)
                .role(role)
                .hashedPassword(password)
                .birthDate(birthDate)
                .build();
        Account playerAccount = Account.builder()
                .playerId(player.getId())
                .currency(USD)
                .build();
        player.setAccount(playerAccount);

        playerService.save(player);
        accountService.save(playerAccount);
        return player;
    }
}

