package me.ryan.simplepluginhome.commands;

import me.ryan.simplepluginhome.config.Config;
import me.ryan.simplepluginhome.user.User;
import me.ryan.simplepluginhome.user.UserService;
import me.ryan.simplepluginhome.user.delay.UserDelayService;
import me.ryan.simplepluginhome.utils.Services;
import me.ryan.simplepluginhome.utils.TimersApi;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandHome implements CommandExecutor {

    private final UserDelayService userDelayService;
    private final UserService userService;
    private final Config config;

    public CommandHome() {
        userDelayService = Services.loadService(UserDelayService.class);
        userService = Services.loadService(UserService.class);
        config = Services.loadService(Config.class);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] a) {
        if (!(s instanceof Player player)) {
            s.sendMessage("§cComando somente para jogadores.");
            return false;
        }

        if (a.length == 0) {
            s.sendMessage("§cSintaxe errada utilize, /" + label + " (home)");
            return false;
        }

        String home = a[0];
        User user = userService.get(s.getName());

        if (!user.isHome(home)) {
            s.sendMessage("§cVocê não possui uma home com este nome.");
            return false;
        }

        if (userDelayService.inCooldown(player.getName(), config.getCooldownMillis())) {
            player.sendMessage(String.format("§cVocê deve aguadar §f%s §cpara ir para sua home.",
                    TimersApi.convertMillis(userDelayService.getDelayRemaining(player.getName(), config.getCooldownMillis()))));
            return false;
        }

        userDelayService.startCooldown(player.getName());
        Location location = user.getHome(home);
        player.teleport(location);
        player.sendMessage("§aTeleportado para a home §f" + home + " §acom sucesso.");

        if (config.isParticlesActivated()) {
            World world = location.getWorld();
            world.spawnParticle(config.getParticleType(), location, config.getParticleAmount());
        }

        return true;
    }

}
