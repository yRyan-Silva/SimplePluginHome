package me.ryan.simplepluginhome.config;

import lombok.Getter;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import me.ryan.simplepluginhome.SimplePluginHome;

@Getter
public class Config {

    private final JavaPlugin plugin;

    private boolean particlesActivated;

    private Particle particleType;

    private int particleAmount;

    private long cooldownMillis;

    public Config() {
        this.plugin = SimplePluginHome.getInstance();
        loadConfig();
    }

    // Pegando a configuração de 'config.yml' e colocando-a em variáveis, para que você não precise acessar o arquivo 'config.yml' diretamente
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        particlesActivated = config.getBoolean("Config.Particles.Activated");
        particleType = Particle.valueOf(config.getString("Config.Particles.Type"));
        particleAmount = config.getInt("Config.Particles.Amount");
        cooldownMillis = config.getInt("Config.Cooldown") * 1000L;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

}
