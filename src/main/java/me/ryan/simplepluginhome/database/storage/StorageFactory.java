package me.ryan.simplepluginhome.database.storage;

import lombok.AllArgsConstructor;
import me.ryan.simplepluginhome.database.connection.ConnectionFactory;
import me.ryan.simplepluginhome.database.connection.impl.file.SQLite;
import me.ryan.simplepluginhome.database.connection.impl.hikari.MySQLAndMariaDB;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.logging.Level;

@AllArgsConstructor
public class StorageFactory {

    private JavaPlugin plugin;
    private String key;

    public Storage create() {
        FileConfiguration config = plugin.getConfig();
        String typeName = config.getString("Database.Type").toUpperCase(Locale.ROOT);
        StorageType storageType = StorageType.of(typeName);

        ConnectionFactory connectionFactory;
        try {

            switch (storageType) {
                case MYSQL:
                case MARIADB:
                    connectionFactory = new MySQLAndMariaDB(plugin, storageType, config);
                    break;
                case SQLITE:
                    connectionFactory = new SQLite(plugin, storageType);
                    break;
                default:
                    Bukkit.getLogger().log(Level.SEVERE, "An error occurred while attempting to initialize the " + storageType);
                    return null;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "An error occurred while attempting to initialize the " + storageType);
            Bukkit.getPluginManager().disablePlugin(plugin);
            return null;
        }

        return new Storage(plugin, key, connectionFactory, storageType);
    }

}
