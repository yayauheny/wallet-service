package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.core.repository.impl.PlayerRepositoryImpl;
import io.ylab.walletservice.exception.InvalidIdException;
import io.ylab.walletservice.exception.NotFoundException;
import io.ylab.walletservice.testutil.AccountTestBuilder;
import io.ylab.walletservice.testutil.PlayerTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_ACCOUNT;
import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_CURRENCY;
import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_PLAYER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @InjectMocks
    PlayerServiceImpl playerService;
    @Mock
    PlayerRepositoryImpl playerRepository;
    @Mock
    AccountServiceImpl accountService;

    @Test
    @DisplayName("should find existing player by id")
    void shouldFindPlayerById() {
        Long playerId = TEST_PLAYER.getId();
        Optional<Player> expectedPlayer = Optional.of(TEST_PLAYER);

        doReturn(expectedPlayer)
                .when(playerRepository).findById(playerId);

        Optional<Player> actualResult = playerService.findById(playerId);

        assertThat(actualResult).isPresent().isEqualTo(expectedPlayer);
    }

    @Test
    @DisplayName("should return an empty optional when player not found")
    void shouldReturnEmptyOptionalWhenPlayerNotFound() {
        Optional<Player> expectedPlayer = Optional.empty();

        doReturn(expectedPlayer)
                .when(playerRepository).findById(any());

        Optional<Player> actualResult = playerService.findById(0L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should throw an exception if id is null or negative")
    void shouldThrowInvalidIdExceptionIfIdIsInvalid() {
        Long emptyId = null;
        Long negativeId = -1L;

        assertThrows(InvalidIdException.class, () -> playerService.findById(emptyId));
        assertThrows(InvalidIdException.class, () -> playerService.findById(negativeId));
    }

    @Test
    @DisplayName("should return all players")
    void shouldReturnAllPlayers() {
        int expectedSize = 1;

        doReturn(List.of(TEST_PLAYER))
                .when(playerRepository).findAll();

        List<Player> actualResult = playerService.findAll();

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_PLAYER);
    }

    @Test
    @DisplayName("should return an empty list if no players found")
    void shouldReturnAnEmptyListIfNoPlayersFound() {
        doReturn(List.of())
                .when(playerRepository).findAll();

        List<Player> actualResult = playerService.findAll();

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should save and return saved Player")
    void shouldSaveAndReturnPlayer() {
        doReturn(TEST_PLAYER)
                .when(playerRepository).save(TEST_PLAYER);

        Player actualResult = playerService.save(TEST_PLAYER);

        assertThat(actualResult).isNotNull().isEqualTo(TEST_PLAYER);
    }

    @Test
    @DisplayName("should update existing player")
    void shouldUpdateExistingPlayer() {
        Player player = PlayerTestBuilder.aPlayer()
                .withId(100L)
                .withUsername("test-name")
                .build();
        Player updatedPlayer = PlayerTestBuilder.aPlayer()
                .withId(player.getId())
                .withUsername("changed-test-name")
                .build();

        doReturn(player)
                .when(playerRepository).save(player);

        playerService.save(player);
        playerService.update(updatedPlayer);

        doReturn(Optional.of(updatedPlayer))
                .when(playerRepository).findById(updatedPlayer.getId());

        Optional<Player> actualResult = playerService.findById(updatedPlayer.getId());

        assertThat(actualResult).isPresent().isEqualTo(Optional.of(updatedPlayer));
    }

    @Test
    @DisplayName("should return all player transactions")
    void shouldReturnAllPlayerTransactions() throws NotFoundException {
        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .id(1L)
                        .amount(new BigDecimal("300"))
                        .currency(TEST_CURRENCY)
                        .participantAccount(TEST_ACCOUNT)
                        .type(TransactionType.CREDIT)
                        .build(),
                Transaction.builder()
                        .id(1L)
                        .amount(new BigDecimal("100"))
                        .currency(TEST_CURRENCY)
                        .participantAccount(TEST_ACCOUNT)
                        .type(TransactionType.DEBIT)
                        .build()
        );
        Account account = AccountTestBuilder.anAccount()
                .withPlayerId(TEST_PLAYER.getId())
                .withId(999L)
                .withCurrentBalance(new BigDecimal("200"))
                .withCurrency(TEST_CURRENCY)
                .build();
        account.setTransactions(transactions);

        doReturn(Optional.of(account))
                .when(accountService).findByPlayerId(account.getPlayerId());

        List<Transaction> actualResult = playerService.getTransactions(account.getPlayerId());

        assertThat(actualResult).isNotNull().isEqualTo(transactions);
    }

    @Test
    @DisplayName("should delete player")
    void shouldDeletePlayer() {
        Player player = PlayerTestBuilder.aPlayer()
                .withId(100L)
                .withUsername("test-name")
                .build();

        doReturn(player)
                .when(playerRepository).save(player);
        doReturn(Optional.empty())
                .when(playerRepository).findById(player.getId());

        Player savedPlayer = playerService.save(player);
        Optional<Player> actualResult = playerService.findById(player.getId());

        assertThat(savedPlayer).isNotNull().isEqualTo(player);
        assertThat(actualResult).isEmpty();
    }
}