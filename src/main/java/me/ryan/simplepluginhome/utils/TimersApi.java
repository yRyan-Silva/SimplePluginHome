package me.ryan.simplepluginhome.utils;

import java.util.concurrent.TimeUnit;

public class TimersApi {

    public static String convertMillis(long millis) {
        if (millis <= 0) return "0s";
        long days = TimeUnit.MILLISECONDS.toDays(millis),
                hours = TimeUnit.MILLISECONDS.toHours(millis) % 24,
                minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60,
                seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        StringBuilder text = new StringBuilder();
        if (days > 0) text.append(days).append("d ");
        if (hours > 0) text.append(hours).append("h ");
        if (minutes > 0) text.append(minutes).append("m ");
        if (seconds > 0 && days <= 0) text.append(seconds).append("s");
        return text.isEmpty() ? "0s" : text.toString().endsWith(" ") ? text.substring(0, text.toString().length() - 1) : text.toString();
    }

}
