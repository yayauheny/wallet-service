package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Player;
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

    @Override
    public Player build() {
        return Player.builder()
                .id(id)
                .username(username)
                .birthDate(birthDate)
                .build();
    }
}