package yayauheny.service.command;

import yayauheny.entity.Player;
import yayauheny.entity.PlayerRole;
import yayauheny.utils.DateTimeUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


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
        try (Scanner sc = new Scanner(System.in)) {
            long idFromConsole = sc.nextLong();
            Optional<Player> maybePlayer = players.stream().filter(p -> p.getId().equals(idFromConsole)).findAny();

            if (maybePlayer.isPresent()) {
                updatePlayer(maybePlayer.get());
            } else {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException e) {
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
        try (Scanner sc = new Scanner(System.in)) {
            byte choice = sc.nextByte();
            switch (choice) {
                case 1 -> {
                    System.out.println("������� ����� ���:");
                    String inputName = sc.nextLine();
                    player.setUsername(inputName);
                    playerService.update(player);
                }
                case 2 -> {
                    System.out.println("������� ����� ���� �������� (����.��.��):");
                    String inputDate = sc.nextLine();
                    LocalDate date = LocalDate.parse(inputDate, DateTimeUtils.dateFormatter);
                    player.setBirthDate(date);
                    playerService.update(player);
                }
                case 3 -> {
                    System.out.println("�������� ���� ������:\n1 - ������������\n2 - �������������");
                    byte inputRole = sc.nextByte();
                    switch (inputRole) {
                        case 1 -> {
                            player.setRole(PlayerRole.USER);
                            playerService.update(player);
                        }
                    }
                }
            }
            System.out.println("������ ��������");
        } catch (InputMismatchException | DateTimeParseException e) {
            System.err.println("������������ ����, ���������� �����");
            updatePlayer(player);
        }
    }
}

