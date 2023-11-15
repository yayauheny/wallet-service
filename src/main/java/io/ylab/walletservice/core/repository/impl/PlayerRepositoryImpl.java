package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.PlayerRole;
import io.ylab.walletservice.core.repository.PlayerRepository;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.infrastructure.database.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@link PlayerRepositoryImpl} class implements the {@code PlayerRepository} interface
 * for accessing and manipulating player entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerRepositoryImpl implements PlayerRepository<Long> {

    private static final PlayerRepositoryImpl INSTANCE = new PlayerRepositoryImpl();

    public static PlayerRepositoryImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> findById(Long id) throws DatabaseException {
        String query = """
                SELECT * FROM players
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildPlayer(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> findByUsername(String username) throws DatabaseException {
        String query = """
                SELECT * FROM players
                WHERE username=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildPlayer(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> findAll() throws DatabaseException {
        String query = """
                SELECT * FROM players;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Player> players = new ArrayList<>();

            while (resultSet.next()) {
                players.add(buildPlayer(resultSet));
            }
            return players;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player save(Player player) throws DatabaseException {
        String query = """
                INSERT INTO players (username, player_role, birth_date, password)
                VALUES (?,?,?,?);
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getRole().name());
            preparedStatement.setObject(3, player.getBirthDate());
            preparedStatement.setBytes(4, player.getHashedPassword());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                player.setId(keys.getObject("id", Long.class));
            }

            return player;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Player player) throws DatabaseException {
        String query = """
                UPDATE players
                SET username=?,
                    player_role=?,
                    birth_date=?,
                    password=?
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getRole().name());
            preparedStatement.setObject(3, player.getBirthDate());
            preparedStatement.setBytes(4, player.getHashedPassword());
            preparedStatement.setLong(5, player.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Player player) throws DatabaseException {
        String query = """
                DELETE FROM players
                WHERE id=?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, player.getId());
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private static Player buildPlayer(ResultSet resultSet) throws SQLException {
        return Player.builder()
                .id(resultSet.getLong("id"))
                .username(resultSet.getString("username"))
                .role(PlayerRole.valueOf(resultSet.getString("player_role")))
                .birthDate(resultSet.getDate("birth_date").toLocalDate())
                .hashedPassword(resultSet.getBytes("password"))
                .build();
    }
}

