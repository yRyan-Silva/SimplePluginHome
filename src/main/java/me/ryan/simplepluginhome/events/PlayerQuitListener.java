package me.ryan.simplepluginhome.events;

import me.ryan.simplepluginhome.user.UserService;
import me.ryan.simplepluginhome.utils.Services;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerQuitListener implements Listener {

    private final UserService userService;

    public PlayerQuitListener() {
        userService = Services.loadService(UserService.class);
    }

    @EventHandler
    void onJoin(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        userService.save(player.getName(), true); // Remover o player da lista para evitar consumir memória pois o jogador saiu do servidor
    }

}