package me.ryan.simplepluginhome.user;

import me.ryan.simplepluginhome.database.storage.Storage;
import me.ryan.simplepluginhome.utils.Services;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserService {

    private final UserRegistry userRegistry;
    private final Storage storage;

    public UserService() {
        userRegistry = Services.loadService(UserRegistry.class);
        storage = Services.loadService(Storage.class);

        for (Player player : Bukkit.getOnlinePlayers()) load(player.getName());
    }

    public void load(String player) {
        storage.get(player).whenCompleteAsync((user, error) -> {
            if (error != null) throw new RuntimeException(error);

            if (user == null) createNewUserAndRegister(player);
            else register(user);
        });
    }

    private User createNewUserAndRegister(String player) {
        User user = new User(player);
        register(user);
        return user;
    }

    private void register(User user) {
        userRegistry.register(user.getPlayer(), user);
    }

    public User get(String player) {
        User user = userRegistry.getByKey(player);
        if (user == null) user = createNewUserAndRegister(player);
        return user;
    }

    public void save(String player, boolean remove) {
        save(get(player), remove);
    }

    public void save(User user, boolean remove) {
        storage.save(user);

        if (remove) userRegistry.unregister(user.getPlayer());
    }

    public void saveAll(boolean shutdown) {
        storage.save(userRegistry.getValues(), shutdown);
    }

}
