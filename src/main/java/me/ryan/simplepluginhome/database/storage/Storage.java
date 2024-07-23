package me.ryan.simplepluginhome.database.storage;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import me.ryan.simplepluginhome.database.async.Async;
import me.ryan.simplepluginhome.database.connection.ConnectionFactory;
import me.ryan.simplepluginhome.user.User;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Storage {

    // Comandos do SQL
    private final String defaultInsert, nameTable, primaryKey;
    private final Map<StorageType, String> insert;
    private final String select, delete, selectAll;

    private final ConnectionFactory connectionFactory;
    private final StorageType storageType;
    private final Gson gson;

    public Storage(JavaPlugin plugin, String primaryKey, ConnectionFactory connectionFactory, StorageType storageType) {
        nameTable = plugin.getName().toLowerCase(Locale.ROOT);
        this.primaryKey = primaryKey;

        defaultInsert = "INSERT INTO " + nameTable + "(" + primaryKey + ", json) VALUES(?, ?) ON DUPLICATE KEY UPDATE json=VALUES(json)";

        insert = ImmutableMap.of(StorageType.SQLITE, "INSERT OR REPLACE INTO " + nameTable + " (" + primaryKey + ", json) VALUES(?, ?)");

        select = "SELECT * FROM " + nameTable + " WHERE " + primaryKey + " = ? LIMIT 1";
        delete = "DELETE FROM " + nameTable + " WHERE " + primaryKey + " = ?";
        selectAll = "SELECT * FROM " + nameTable;

        this.connectionFactory = connectionFactory;
        this.storageType = storageType;
        this.gson = new Gson();
    }

    public void createTable() {
        Async.run(() -> {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + nameTable + " ("
                         + primaryKey + " VARCHAR(36) NOT NULL PRIMARY KEY, json LONGTEXT NOT NULL);")) {
                ps.executeUpdate();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public void save(User user) {
        if (!user.isDirty()) return;

        Async.run(() -> {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statement = connection.prepareStatement(insert.getOrDefault(storageType, defaultInsert))) {

                user.setDirty(false);
                statement.setString(1, user.getPlayer());
                statement.setString(2, gson.toJson(user));

                statement.executeUpdate();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public void save(List<User> users, boolean shutdown) {
        Async.run(() -> {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statement = connection.prepareStatement(insert.getOrDefault(storageType, defaultInsert))) {

                for (User user : users) {
                    if (!user.isDirty()) continue;
                    user.setDirty(false);

                    statement.setString(1, user.getPlayer());
                    statement.setString(2, gson.toJson(user));
                    statement.addBatch();
                }

                statement.executeBatch();

                if (shutdown) shutdown();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public CompletableFuture<User> get(String primaryKey) {
        return Async.run(() -> {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statementFarmCactus = connection.prepareStatement(select)) {

                statementFarmCactus.setString(1, primaryKey);
                ResultSet resultFarmCactus = statementFarmCactus.executeQuery();

                if (resultFarmCactus.next())
                    return gson.fromJson(resultFarmCactus.getString("json"), User.class);
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
            return null;
        });
    }

    public CompletableFuture<List<User>> getAll() {
        return Async.run(() -> {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statementFarmCactus = connection.prepareStatement(selectAll)) {
                List<User> list = new ArrayList<>();

                ResultSet resultSet = statementFarmCactus.executeQuery();

                while (resultSet.next()) {
                    String json = resultSet.getString("json");
                    list.add(gson.fromJson(json, User.class));
                }

                return list;
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public void delete(String primaryKey) {
        Async.run(() -> {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statement = connection.prepareStatement(delete)) {
                statement.setString(1, primaryKey);
                statement.executeUpdate();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public void shutdown() {
        this.connectionFactory.close();
    }

}
