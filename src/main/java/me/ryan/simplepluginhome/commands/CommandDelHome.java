package me.ryan.simplepluginhome.commands;

import me.ryan.simplepluginhome.user.User;
import me.ryan.simplepluginhome.user.UserService;
import me.ryan.simplepluginhome.utils.Services;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandDelHome implements CommandExecutor {

    private final UserService userService;

    public CommandDelHome() {
        userService = Services.loadService(UserService.class);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] a) {
        if (!(s instanceof Player player)) {
            s.sendMessage("§cComando somente para jogadores.");
            return false;
        }

        if (!s.hasPermission("simplepluginhome.delhome")) {
            s.sendMessage("§cVocê não possui permissão para executar este comando.");
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

        user.removeHome(home);
        player.sendMessage("§cHome §f" + home + " §cdeletada com sucesso.");

        return true;
    }

}