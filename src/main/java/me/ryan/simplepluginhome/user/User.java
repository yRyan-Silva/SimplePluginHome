package me.ryan.simplepluginhome.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.ryan.simplepluginhome.utils.LocationSerializer;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@AllArgsConstructor
@Data
public class User {

    private final String player;
    private final Map<String, String> homes;

    private transient boolean dirty; // Para somente salvar no banco de dados caso tenha alguma alteração na classe

    public User(String player) {
        this.player = player;
        this.homes = new HashMap<>();
    }

    public boolean isHome(@NotNull String name) {
        return this.homes.containsKey(name.toLowerCase(Locale.ROOT));
    }

    public void addHome(@NotNull String name, @NotNull Location location) {
        this.homes.put(name.toLowerCase(Locale.ROOT), LocationSerializer.serializeLocation(location));
        this.dirty = true;
    }

    public void removeHome(@NotNull String name) {
        this.homes.remove(name.toLowerCase(Locale.ROOT));
        this.dirty = true;
    }

    public Location getHome(@NotNull String name) {
        String locationSerialized = homes.get(name.toLowerCase(Locale.ROOT));
        return locationSerialized == null ? null : LocationSerializer.deserializeLocation(locationSerialized);
    }

}
