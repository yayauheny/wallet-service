//package io.ylab.walletservice.core.service.impl;
//
//import io.ylab.walletservice.core.domain.Player;
//import io.ylab.walletservice.core.domain.PlayerRole;
//import io.ylab.walletservice.core.domain.Transaction;
//import io.ylab.walletservice.exception.DatabaseException;
//import io.ylab.walletservice.exception.InvalidIdException;
//import io.ylab.walletservice.testutil.PlayerTestBuilder;
//import io.ylab.walletservice.testutil.PostgresTestcontainer;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static io.ylab.walletservice.testutil.TestObjectsUtil.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//@ExtendWith(MockitoExtension.class)
//class PlayerServiceImplTest {
//
//    static CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
//    static PlayerServiceImpl playerService = new PlayerServiceImpl();
//    static AccountServiceImpl accountService = new AccountServiceImpl();
//    static TransactionServiceImpl transactionService = new TransactionServiceImpl();
//
//    @BeforeAll
//    static void startContainer() {
//        PostgresTestcontainer.init();
//        createObjects(currencyService, playerService, accountService);
//    }
//
//    @AfterAll
//    static void closeContainer() {
//        PostgresTestcontainer.close();
//    }
//
//    @Test
//    @DisplayName("should find existing player by id")
//    void shouldFindPlayerById() throws DatabaseException {
//        Optional<Player> expectedPlayer = Optional.of(TEST_PLAYER_IVAN);
//        Optional<Player> actualResult = playerService.findById(TEST_PLAYER_IVAN.getId());
//
//        assertThat(actualResult).isPresent().isEqualTo(expectedPlayer);
//    }
//
//    @Test
//    @DisplayName("should return an empty optional when player not found")
//    void shouldReturnEmptyOptionalWhenPlayerNotFound() throws DatabaseException {
//        Optional<Player> actualResult = playerService.findById(9999L);
//
//        assertThat(actualResult).isEmpty();
//    }
//
//    @Test
//    @DisplayName("should throw an exception if id is null or negative")
//    void shouldThrowInvalidIdExceptionIfIdIsInvalid() {
//        Long emptyId = null;
//        Long negativeId = -1L;
//
//        assertThrows(InvalidIdException.class, () -> playerService.findById(emptyId));
//        assertThrows(InvalidIdException.class, () -> playerService.findById(negativeId));
//    }
//
//    @Test
//    @DisplayName("should return all players")
//    void shouldReturnAllPlayers() throws DatabaseException {
//        int expectedSize = 3;
//        List<Player> actualResult = playerService.findAll();
//
//        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_PLAYER_IVAN, TEST_PLAYER_ANDREW, TEST_PLAYER_HANNA);
//    }
//
//    @Test
//    @DisplayName("should save and return saved Player")
//    void shouldSaveAndReturnPlayer() throws DatabaseException {
//        Player player = PlayerTestBuilder.aPlayer()
//                .withRole(PlayerRole.ADMIN)
//                .withUsername("dummy1")
//                .withPassword("dummy1")
//                .withBirthDate(LocalDate.now())
//                .build();
//        Player actualResult = playerService.save(player);
//
//        assertThat(actualResult).isNotNull().isEqualTo(player);
//    }
//
//    @Test
//    @DisplayName("should update existing player")
//    void shouldUpdateExistingPlayer() throws DatabaseException {
//        TEST_PLAYER_IVAN.setUsername("updated-ivan");
//        playerService.update(TEST_PLAYER_IVAN);
//
//        Optional<Player> actualResult = playerService.findById(TEST_PLAYER_IVAN.getId());
//
//        assertThat(actualResult).isPresent().isEqualTo(Optional.of(TEST_PLAYER_IVAN));
//    }
//
//    @Test
//    @DisplayName("should return all player transactions")
//    void shouldReturnAllPlayerTransactions() throws DatabaseException {
//        int size = 1;
//        TEST_TRANSACTION.setParticipantAccountId(TEST_ACCOUNT_IVAN.getId());
//        List<Transaction> transactions = new ArrayList<>(Arrays.asList(TEST_TRANSACTION));
//
//        transactionService.save(TEST_TRANSACTION, TEST_ACCOUNT_IVAN);
//
//        List<Transaction> actualResult = playerService.getTransactions(TEST_ACCOUNT_IVAN.getPlayerId());
//
//        assertThat(actualResult).hasSize(1);
//        assertThat(actualResult).isNotEmpty().isEqualTo(transactions);
//    }
//
//    @Test
//    @DisplayName("should delete player")
//    void shouldDeletePlayer() throws DatabaseException {
//        playerService.delete(TEST_PLAYER_HANNA);
//        Optional<Player> actualResult = playerService.findById(TEST_PLAYER_HANNA.getId());
//
//        assertThat(actualResult).isEmpty();
//    }
//}