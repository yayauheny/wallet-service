package io.ylab.walletservice.api;

import io.ylab.walletservice.core.domain.PlayerRole;
import io.ylab.walletservice.core.service.command.Command;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PermissionsManagerTest {

    @Test
    @DisplayName("should return only allowed commands")
    void shouldReturnAllowedCommandsBasedOnRole() {
        List<Command> userCommands = PermissionsManager.getCommands(PlayerRole.USER);
        List<Command> adminCommands = PermissionsManager.getCommands(PlayerRole.ADMIN);

        assertThat(userCommands).hasSize(2);
        assertThat(adminCommands).hasSize(6);
    }
}