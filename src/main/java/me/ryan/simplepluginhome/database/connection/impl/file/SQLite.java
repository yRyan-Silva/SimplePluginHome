package me.ryan.simplepluginhome.database.connection.impl.file;

import lombok.SneakyThrows;
import me.ryan.simplepluginhome.database.connection.ConnectionFactory;
import me.ryan.simplepluginhome.database.storage.StorageType;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLite implements ConnectionFactory {

    private final NonClosableConnection connection;

    public SQLite(JavaPlugin plugin, StorageType storageType) throws Exception {
        Path file = plugin.getDataFolder().toPath().resolve("database.sqlite.db");

        String url = "jdbc:sqlite:" + file;

        Class.forName(storageType.getDriver());
        this.connection = new NonClosableConnection(DriverManager.getConnection(url));
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @SneakyThrows
    @Override
    public void close() {
        connection.shutdown();
    }

}
