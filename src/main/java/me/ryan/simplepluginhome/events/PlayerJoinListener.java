package me.ryan.simplepluginhome.events;

import me.ryan.simplepluginhome.user.UserService;
import me.ryan.simplepluginhome.utils.Services;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final UserService userService;

    public PlayerJoinListener() {
        userService = Services.loadService(UserService.class);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        userService.load(player.getName());
    }

}
