package io.ylab.walletservice.infrastructure.database;

import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.util.PropertiesUtil;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

@UtilityClass
public class ConnectionManager {

    private final String EXCEPTION_MESSAGE = "Cannot release connection. Something gone wrong";
    private final Queue<Connection> CONNECTIONS;
    private final String URL;
    private final String USER;
    private final String PASSWORD;
    private final int RELEASE_TIMEOUT;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        URL = PropertiesUtil.get("db.connection.url");
        USER = PropertiesUtil.get("db.connection.username");
        PASSWORD = PropertiesUtil.get("db.connection.password");
        RELEASE_TIMEOUT = Integer.parseInt(PropertiesUtil.get("db.connection.releaseTimeout"));

        int poolSize = Integer.parseInt(PropertiesUtil.get("db.connection.poolSize"));
        CONNECTIONS = new ArrayBlockingQueue<>(poolSize);

        IntStream.range(0, poolSize)
                .forEach(i -> {
                    try {
                        CONNECTIONS.add(DriverManager.getConnection(URL, USER, PASSWORD));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Connection getConnection() throws DatabaseException {
        if (!CONNECTIONS.isEmpty()) {
            return CONNECTIONS.poll();
        } else {
            try {
                Thread.sleep(RELEASE_TIMEOUT);
            } catch (InterruptedException e) {
                throw new DatabaseException(EXCEPTION_MESSAGE);
            }

            Connection connection = CONNECTIONS.poll();
            if (connection == null) {
                throw new DatabaseException(EXCEPTION_MESSAGE);
            } else {
                return connection;
            }
        }
    }

    public void releaseConnection(Connection connection) throws DatabaseException {
        if (connection == null) {
            throw new DatabaseException(EXCEPTION_MESSAGE);
        }
        CONNECTIONS.add(connection);
    }
}
