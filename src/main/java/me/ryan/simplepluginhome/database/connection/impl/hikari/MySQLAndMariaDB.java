package me.ryan.simplepluginhome.database.connection.impl.hikari;

import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.ryan.simplepluginhome.database.connection.ConnectionFactory;
import me.ryan.simplepluginhome.database.storage.StorageType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class MySQLAndMariaDB implements ConnectionFactory {

    private final HikariDataSource hikariDataSource;
    private final JavaPlugin plugin;

    public MySQLAndMariaDB(JavaPlugin plugin, StorageType storageType, FileConfiguration config) {
        this.plugin = plugin;
        String host = config.getString("Database.Ip") + ":" + config.getInt("Database.Port");
        String database = config.getString("Database.Database");

        String url = "jdbc:" + storageType.name().toLowerCase(Locale.ROOT) + "://" + host + "/" + database + "?autoReconnect=true&tcpKeepAlive=true";

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(config.getString("Database.User"));
        hikariConfig.setPassword(config.getString("Database.Password"));
        hikariConfig.setPoolName(plugin.getName().toLowerCase(Locale.ROOT) + "_pool");
        hikariConfig.setDriverClassName(storageType.getDriver());

        Map<String, String> properties = Maps.newHashMap();

        for (String key : config.getConfigurationSection("Database.Properties").getKeys(false))
            properties.put(key, config.getString("Database.Properties." + key));

        properties.putIfAbsent("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        properties.forEach(hikariConfig::addDataSourceProperty);

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "An error occurred while trying to initialize the database connection");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return null;
        }
    }

    @Override
    public void close() {
        try {
            hikariDataSource.close();
        } catch (Exception ignored) {
        }
    }

}
