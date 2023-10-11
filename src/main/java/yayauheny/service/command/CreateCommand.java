package yayauheny.service.command;

import yayauheny.entity.Account;
import yayauheny.entity.Currency;
import yayauheny.entity.Player;
import yayauheny.entity.PlayerRole;
import yayauheny.service.impl.Auditor;
import yayauheny.utils.DateTimeUtils;
import yayauheny.utils.PasswordHasher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;


/**
 * The {@link CreateCommand} class is an implementation of the {@link Command} interface,
 * allowing users to register a new player.
 */
public class CreateCommand implements Command {

    /**
     * The predefined currency (USD) for the player account.
     */
    private static final Currency USD = new Currency(BigDecimal.ONE, "USD");

    /**
     * Executes the create command to register a new player.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) {
        System.out.println("Регистрация нового игрока:");
        System.out.println("Введите имя:");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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

            Player createdPlayer = registerPlayer(inputName, role, PasswordHasher.hashPassword(inputPassword), birthDate);
            System.out.println("Аккаунт был успешно зарегистрирован.");
            Auditor.log("player: %s has been created".formatted(createdPlayer.getUsername()));
        } catch (InputMismatchException | DateTimeParseException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            execute(player);
        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
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
    private Player registerPlayer(String username, PlayerRole role, byte[] password, LocalDate birthDate) {
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

