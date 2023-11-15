package io.ylab.walletservice.infrastructure.database;

import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.util.PropertiesUtil;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

/**
 * The {@code ConnectionManager} class is responsible for managing database connections in the application.
 * It provides functionality to create, retrieve, and release database connections, optimizing resource utilization
 * through connection pooling.
 * <p>
 * This class is a utility class with static methods, and it ensures that a pool of connections is available for efficient
 * interaction with the database.
 * </p>
 */
@UtilityClass
public class ConnectionManager {

    /**
     * The constant string representing the exception message used when releasing a connection encounters an error.
     */
    private static final String EXCEPTION_MESSAGE = "Cannot release connection. Something gone wrong";

    /**
     * A blocking queue that holds proxied database connections, serving as the connection pool.
     */
    private static BlockingQueue<Connection> pool;

    /**
     * A blocking queue that stores original database connections created by the manager.
     */
    private static BlockingQueue<Connection> sourceConnections;

    /**
     * A string representing the JDBC connection URL for the database.
     */
    private static String url;

    /**
     * A string representing the username for connecting to the database.
     */
    private static String user;

    /**
     * A string representing the password for connecting to the database.
     */
    private static String password;

    /**
     * An integer representing the timeout value for releasing connections.
     */
    private static int releaseTimeout;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reloads the database configuration, initializing the connection pool and other related settings based on environment property.
     */
    public static void reloadConfiguration() {
        String environment = System.getProperty("app.environment", "dev");

        url = PropertiesUtil.get("db.connection.url." + environment);
        user = PropertiesUtil.get("db.connection.username." + environment);
        password = PropertiesUtil.get("db.connection.password." + environment);
        releaseTimeout = Integer.parseInt(PropertiesUtil.get("db.connection.releaseTimeout." + environment));
        int poolSize = Integer.parseInt(PropertiesUtil.get("db.connection.poolSize." + environment));
        pool = new ArrayBlockingQueue<>(poolSize);
        sourceConnections = new ArrayBlockingQueue<>(poolSize);

        IntStream.range(0, poolSize)
                .forEach(i -> {
                    Connection connection = open();
                    Connection proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                            (proxy, method, args) -> method.getName().equals("close")
                                    ? pool.add((Connection) proxy)
                                    : method.invoke(connection, args)
                    );
                    pool.add(proxyConnection);
                    sourceConnections.add(connection);
                });
    }

    /**
     * Retrieves a database connection from the connection pool. If the pool is empty, it waits for a connection to become available.
     *
     * @return A database connection.
     * @throws DatabaseException If an error occurs while retrieving a connection.
     */
    public Connection getConnection() throws DatabaseException {
        if (!pool.isEmpty()) {
            try {
                return pool.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Thread.sleep(releaseTimeout);
            } catch (InterruptedException e) {
                throw new DatabaseException(EXCEPTION_MESSAGE, e);
            }

            Connection connection = pool.poll();
            if (connection == null) {
                throw new DatabaseException(EXCEPTION_MESSAGE);
            } else {
                return connection;
            }
        }
    }

    /**
     * Closes the entire connection pool, releasing all database connections.
     */
    public void closeConnectionPool() {
        for (Connection connection : sourceConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Opens a new database connection using the configured URL, username, and password.
     *
     * @return A new database connection.
     */
    private Connection open() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
