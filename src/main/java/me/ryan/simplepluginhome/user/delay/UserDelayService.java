package me.ryan.simplepluginhome.user.delay;

import me.ryan.simplepluginhome.utils.Services;

public class UserDelayService {

    private final UserDelayRegistry userDelayRegistry;

    public UserDelayService() {
        userDelayRegistry = Services.loadService(UserDelayRegistry.class);
    }

    public long getDelayRemaining(String player, long delayMillis) {
        long value = userDelayRegistry.getByKey(player, -1L);
        if (value == -1) return 0;

        return (value + delayMillis) - System.currentTimeMillis();
    }

    public boolean inCooldown(String player, long delayMillis) {
        long value = userDelayRegistry.getByKey(player, -1L);
        if (value == -1) return false;

        return System.currentTimeMillis() - value <= delayMillis;
    }

    public void startCooldown(String player) {
        userDelayRegistry.register(player, System.currentTimeMillis());
    }

}