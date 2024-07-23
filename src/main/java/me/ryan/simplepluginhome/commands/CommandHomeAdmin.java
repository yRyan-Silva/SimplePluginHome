package me.ryan.simplepluginhome.commands;

import me.ryan.simplepluginhome.config.Config;
import me.ryan.simplepluginhome.utils.Services;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandHomeAdmin implements CommandExecutor {

    private final Config config;

    public CommandHomeAdmin() {
        config = Services.loadService(Config.class);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] a) {
        if (!s.hasPermission("simplepluginhome.admin")) {
            s.sendMessage("§cVocê não possui permissão para executar este comando.");
            return false;
        }

        if (a.length == 0 || !a[0].equalsIgnoreCase("reload")) {
            s.sendMessage("§cSintaxe errada utilize, /" + label + " reload");
            return false;
        }

        config.reloadConfig();
        s.sendMessage("§aConfiguração recarregada com sucesso.");

        return true;
    }

}
