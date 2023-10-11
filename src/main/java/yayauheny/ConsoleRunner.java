package yayauheny;

import yayauheny.entity.Account;
import yayauheny.entity.Currency;
import yayauheny.entity.Player;
import yayauheny.entity.PlayerRole;
import yayauheny.service.command.Command;
import yayauheny.service.impl.AccountServiceImpl;
import yayauheny.service.impl.PlayerServiceImpl;
import yayauheny.utils.DateTimeUtils;
import yayauheny.utils.PasswordHasher;
import yayauheny.utils.PermissionsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    /**
     * The main method to start the console-based user interface.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) throws IOException {
        Player player = authenticate();
        authorize(player);
        reader.close();
    }

    /**
     * Authenticates the user by providing login or registration options.
     *
     * @return The authenticated player.
     */
    private static Player authenticate() {
        String authMessage = """
                Войдите или зарегистрируйтесь в системе, чтобы продолжить работу:
                1 - войти
                2 - зарегистрироваться
                3 - выйти
                """;
        String incorrectInput = "Некорректный ввод, попробуйте снова";

        try {
            System.out.println(authMessage);
            int firstChoice = Integer.parseInt(reader.readLine());
            return switch (firstChoice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> null;
                default -> throw new InputMismatchException();
            };
        } catch (InputMismatchException | IOException e) {
            System.err.println(incorrectInput);
            return authenticate();
        }
    }

    /**
     * Authorizes the user and allows the execution of various commands based on their role.
     *
     * @param player The authenticated player.
     */
    private static void authorize(Player player) {
        System.out.println("Добро пожаловать в систему, " + player.getUsername() + "\nСписок доступных действий:");
        List<Command> commands = PermissionsManager.getCommands(player.getRole());
        for (int i = 0; i < commands.size(); i++) {
            System.out.println(i + 1 + " - " + commands.get(i).getName());
        }
        System.out.println("выход - выйти\n\n");
        System.out.println("Введите команду:");
        try {
            String input = reader.readLine();

            int choice = Integer.parseInt(input);
            if (choice > 0 && choice <= commands.size()) {
                commands.get(choice - 1).execute(player);
            } else {
                System.out.println("Некорректный ввод");
            }

        } catch (IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            authorize(player);
        }
    }

    /**
     * Logs in an existing player by providing a username and password.
     *
     * @return The logged-in player.
     */
    private static Player login() {
        try {
            System.out.println("Введите логин:");
            String loginInput = reader.readLine();
            System.out.println("Введите пароль:");
            String password = reader.readLine();

            return processLoginCredentials(loginInput, password);
        } catch (InputMismatchException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            return login();
        }
    }

    /**
     * Registers a new player by providing user details.
     *
     * @return The newly registered player.
     */
    private static Player register() {
        return registrateNewPlayer();
    }

    /**
     * Processes the login credentials provided by the user.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return The authenticated player.
     */
    private static Player processLoginCredentials(String username, String password) {
        Optional<Player> maybePlayer = playerService.findByUsername(username);

        if (maybePlayer.isPresent()) {
            Player player = maybePlayer.get();
            byte[] hashedPassword = player.getHashedPassword();
            boolean isMatch = PasswordHasher.checkPassword(password, hashedPassword);
            if (isMatch) {
                return player;
            } else {
                System.out.println("Неверные данные, попробуйте снова");
                return authenticate();
            }
        } else {
            System.out.println("Такого пользователя не существует, зарегистрируйтесь или проверьте логин");
            return authenticate();
        }
    }

    /**
     * Registers a new player by providing user details.
     *
     * @return The newly registered player.
     */
    private static Player registrateNewPlayer() {
        System.out.println("Регистрация нового игрока:");
        System.out.println("Введите имя:");

        try {
            String inputName = reader.readLine();
            System.out.println("Введите дату рождения (гггг.мм.дд)");
            String inputDate = reader.readLine();
            LocalDate birthDate = LocalDate.parse(inputDate, DateTimeUtils.dateFormatter);

            System.out.println("Выберите роль игрока:\n1 - пользователь\n2 - администратор");
            int inputRole = Integer.parseInt(reader.readLine());

            PlayerRole role;
            switch (inputRole) {
                case 1 -> role = PlayerRole.USER;
                case 2 -> role = PlayerRole.ADMIN;
                default -> throw new InputMismatchException();
            }

            System.out.println("Введите пароль:");
            String inputPassword = reader.readLine();

            return createNewPlayer(inputName, role, PasswordHasher.hashPassword(inputPassword), birthDate);
        } catch (InputMismatchException | DateTimeParseException | NumberFormatException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            return registrateNewPlayer();
        }
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

