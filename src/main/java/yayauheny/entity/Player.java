package yayauheny.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private static long idCounter = 0;
    private Long id;
    private PlayerRole role;
    private String username;
    private LocalDate birthDate;
    private byte[] hashedPassword;
    private Account account;

    @Builder
    public Player(PlayerRole role, String username, LocalDate birthDate, byte[] hashedPassword, Account account) {
        this.id = idCounter++;
        this.role = role;
        this.username = username;
        this.birthDate = birthDate;
        this.hashedPassword = hashedPassword;
        this.account = account;
    }
}
