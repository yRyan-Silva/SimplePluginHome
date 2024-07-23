package me.ryan.simplepluginhome.commands;

import me.ryan.simplepluginhome.config.Config;
import me.ryan.simplepluginhome.user.User;
import me.ryan.simplepluginhome.user.UserService;
import me.ryan.simplepluginhome.utils.Services;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

public class CommandSetHome implements CommandExecutor {

    private final UserService userService;
    private final Config config;

    public CommandSetHome() {
        userService = Services.loadService(UserService.class);
        config = Services.loadService(Config.class);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] a) {
        if (!(s instanceof Player player)) {
            s.sendMessage("§cComando somente para jogadores.");
            return false;
        }

        if (!s.hasPermission("simplepluginhome.sethome")) {
            s.sendMessage("§cVocê não possui permissão para executar este comando.");
            return false;
        }

        if (a.length == 0) {
            s.sendMessage("§cSintaxe errada utilize, /" + label + " (home)");
            return false;
        }

        String home = a[0];
        User user = userService.get(s.getName());

        int playerMaxHome = getPlayerMaxHome(player);
        if (user.getHomeAmount() >= playerMaxHome) {
            s.sendMessage(String.format("§cVocê só pode ter no máximo §f%s §chomes.", playerMaxHome));
            return false;
        }

        if (user.isHome(home)) {
            s.sendMessage("§cVocê já possui uma home com este nome.");
            return false;
        }

        user.addHome(home, player.getLocation());
        player.sendMessage("§aHome §f" + home + " §acriada com sucesso.");

        return true;
    }

    private int getPlayerMaxHome(Player player) {
        int homeLimit = config.getDefaultHomeLimit();

        for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
            if (!permission.getPermission().startsWith("simplepluginhome.homes.")) continue;

            try {
                int newLimit = Integer.parseInt(permission.getPermission().replace("simplepluginhome.homes.", ""));
                if (newLimit > homeLimit) homeLimit = newLimit;
            } catch (Throwable e) {
            }
        }

        return homeLimit;
    }

}