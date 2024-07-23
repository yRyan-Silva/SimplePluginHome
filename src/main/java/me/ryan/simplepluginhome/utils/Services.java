package me.ryan.simplepluginhome.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import java.util.Objects;
import java.util.Optional;

@UtilityClass
public final class Services {

    /**
     * Load service class.
     *
     * @param clazz class you want to take
     * @return A {@link T} return class
     * @throws IllegalStateException if the class has not been registered
     */
    public <T> T loadService(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return getService(clazz).orElseThrow(() -> new IllegalStateException("No registration present for service '" + clazz.getName() + "'"));
    }

    /**
     * Get service class.
     *
     * @param clazz class you want to take
     * @return A {@link Optional<T>} return optional class or null if not contains
     */
    private <T> Optional<T> getService(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        RegisteredServiceProvider<T> registration = Bukkit.getServicesManager().getRegistration(clazz);
        return registration == null ? Optional.empty() : Optional.of(registration.getProvider());
    }

    /**
     * Register service class.
     *
     * @param clazz class to be want to register
     * @param instance instance of the class to register
     * @param plugin plugin responsible for registering
     */
    public <T> void registerService(Class<T> clazz, T instance, Plugin plugin) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(instance, "instance");
        Objects.requireNonNull(plugin, "plugin");
        Bukkit.getServicesManager().register(clazz, instance, plugin, ServicePriority.Normal);
    }

}