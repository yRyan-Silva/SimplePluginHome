package me.ryan.simplepluginhome.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + ','
                + location.getX() + ','
                + location.getY() + ','
                + location.getZ() + ','
                + location.getYaw() + ','
                + location.getPitch();
    }

    public static Location deserializeLocation(String s) {
        String[] location = s.split(",");
        return new Location(Bukkit.getWorld(location[0]),
                Double.parseDouble(location[1]),
                Double.parseDouble(location[2]),
                Double.parseDouble(location[3]),
                Float.parseFloat(location[4]),
                Float.parseFloat(location[5]));
    }

}
