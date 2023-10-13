package io.ylab.walletservice.api;

import io.ylab.walletservice.core.domain.PlayerRole;
import io.ylab.walletservice.core.service.command.BalanceOperationsCommand;
import io.ylab.walletservice.core.service.command.Command;
import io.ylab.walletservice.core.service.command.CreateCommand;
import io.ylab.walletservice.core.service.command.DeleteCommand;
import io.ylab.walletservice.core.service.command.EditCommand;
import io.ylab.walletservice.core.service.command.ReadLogsCommand;
import io.ylab.walletservice.core.service.command.ReadTransactionsCommand;
import org.assertj.core.api.Assertions;
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