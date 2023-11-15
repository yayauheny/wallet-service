package io.ylab.walletservice.api;

import io.ylab.walletservice.core.dto.player.PlayerResponse;
import io.ylab.walletservice.util.PropertiesUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class JwtProviderTest {

    @Test
    @DisplayName("should generate valid token")
    void shouldGenerateTokenForPlayerCorrectly() {
        PlayerResponse player = new PlayerResponse(
                1L,
                "USER",
                "admin",
                LocalDate.of(1999, 12, 12).toString()
        );

        String actualResult = JwtProvider.generateTokenForPlayer(player);
        assertThat(actualResult)
                .isNotEmpty()
                .containsPattern("^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*)");
    }

    @Test
    void shouldUpdateTokenSecret() {
        String oldSecret = PropertiesUtil.get("jwt.token.secret");
        JwtProvider.updateSecret();
        String updatedSecret = PropertiesUtil.get("jwt.token.secret");

        assertThat(updatedSecret).isNotEmpty().isNotEqualTo(oldSecret);
    }
}