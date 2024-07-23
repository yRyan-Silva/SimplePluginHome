package me.ryan.simplepluginhome.user.task;

import me.ryan.simplepluginhome.user.UserService;
import me.ryan.simplepluginhome.utils.Services;
import org.bukkit.scheduler.BukkitRunnable;

public class UserSaveAllTask extends BukkitRunnable {

    private final UserService userService;

    public UserSaveAllTask() {
        userService = Services.loadService(UserService.class);
    }

    // Salvar todos usuarios temporariamente para evitar rollbacks
    @Override
    public void run() {
        userService.saveAll(false);
    }

}
