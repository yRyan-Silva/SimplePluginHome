package me.ryan.simplepluginhome;

import me.ryan.simplepluginhome.commands.*;
import me.ryan.simplepluginhome.config.Config;
import me.ryan.simplepluginhome.database.storage.Storage;
import me.ryan.simplepluginhome.database.storage.StorageFactory;
import me.ryan.simplepluginhome.events.*;
import me.ryan.simplepluginhome.user.UserRegistry;
import me.ryan.simplepluginhome.user.UserService;
import me.ryan.simplepluginhome.user.delay.UserDelayRegistry;
import me.ryan.simplepluginhome.user.delay.UserDelayService;
import me.ryan.simplepluginhome.user.task.UserSaveAllTask;
import me.ryan.simplepluginhome.utils.Services;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimplePluginHome extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadDatabaseAndRegisterService();

        registerServices();
        registerCommands();
        registerListeners();

        loadTasks();
    }

    @Override
    public void onDisable() {
        Services.loadService(UserService.class).saveAll(true); // Salvar todos usuários
        HandlerList.unregisterAll(this); // Desregistrar todos eventos para este plugin
    }

    private void registerCommands() {
        getCommand("home").setExecutor(new CommandHome());
        getCommand("delhome").setExecutor(new CommandDelHome());
        getCommand("sethome").setExecutor(new CommandSetHome());
        getCommand("homeadmin").setExecutor(new CommandHomeAdmin());
    }

    private void registerServices() {
        Services.registerService(Config.class, new Config(), this);

        Services.registerService(UserRegistry.class, new UserRegistry(), this);
        Services.registerService(UserService.class, new UserService(), this);

        Services.registerService(UserDelayRegistry.class, new UserDelayRegistry(), this);
        Services.registerService(UserDelayService.class, new UserDelayService(), this);
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
    }

    private void loadDatabaseAndRegisterService() {
        Storage storage = new StorageFactory(this, "player").create();
        storage.createTable();

        Services.registerService(Storage.class, storage, this);
    }

    private void loadTasks() {
        new UserSaveAllTask().runTaskTimerAsynchronously(this, 20 * 20, 20 * 20);
    }

    public static SimplePluginHome getInstance() {
        return getPlugin(SimplePluginHome.class);
    }

}
