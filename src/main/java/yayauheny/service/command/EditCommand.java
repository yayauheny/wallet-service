package yayauheny.service.command;

import yayauheny.entity.Player;
import yayauheny.entity.PlayerRole;
import yayauheny.utils.DateTimeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        return "��������� ������";
    }

    /**
     * Reads the user input for the player's ID to be edited.
     *
     * @param players The list of players to choose from.
     */
    private void readIdFromConsole(List<Player> players) {
        System.out.println("������� ������������� ������������ ��� ���������:");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            long idFromConsole = Long.parseLong(reader.readLine());
            Optional<Player> maybePlayer = players.stream().filter(p -> p.getId().equals(idFromConsole)).findAny();

            if (maybePlayer.isPresent()) {
                updatePlayer(maybePlayer.get());
            } else {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException | IOException e) {
            System.err.println("������������ ����, ���������� �����");
            readIdFromConsole(players);
        }
    }

    /**
     * Updates the player's information based on user input.
     *
     * @param player The player to be updated.
     */
    private void updatePlayer(Player player) {
        System.out.println("�������� ����:\n1 - ���\n2 - ���� ��������\n3 - ����");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            byte choice = Byte.parseByte(reader.readLine());
            switch (choice) {
                case 1 -> {
                    System.out.println("������� ����� ���:");
                    String inputName = reader.readLine();
                    player.setUsername(inputName);
                    playerService.update(player);
                }
                case 2 -> {
                    System.out.println("������� ����� ���� �������� (����.��.��):");
                    String inputDate = reader.readLine();
                    LocalDate date = LocalDate.parse(inputDate, DateTimeUtils.dateFormatter);
                    player.setBirthDate(date);
                    playerService.update(player);
                }
                case 3 -> {
                    System.out.println("�������� ���� ������:\n1 - ������������\n2 - �������������");
                    byte inputRole = Byte.parseByte(reader.readLine());
                    switch (inputRole) {
                        case 1 -> {
                            player.setRole(PlayerRole.USER);
                            playerService.update(player);
                        }
                    }
                }
            }
            System.out.println("������ ��������");
        } catch (InputMismatchException | DateTimeParseException | IOException e) {
            System.err.println("������������ ����, ���������� �����");
            updatePlayer(player);
        }
    }
}

