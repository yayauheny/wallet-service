package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.PlayerRole;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aPlayer")
@With
public class PlayerTestBuilder implements TestBuilder<Player> {

    private Long id = 0L;
    private String username;
    private LocalDate birthDate;
    private PlayerRole role;
    private Account account;
    private String password;

    @Override
    public Player build() {
        return Player.builder()
                .id(id)
                .username(username)
                .birthDate(birthDate)
                .role(role)
                .account(account)
                .hashedPassword(password.getBytes())
                .build();
    }
}