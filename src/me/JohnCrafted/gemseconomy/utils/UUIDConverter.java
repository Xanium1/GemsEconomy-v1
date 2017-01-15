package me.JohnCrafted.gemseconomy.utils;

import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Created by John on 09.04.2016.
 */
public class UUIDConverter {

    public static UUID convertUsernameToUUID(String str)
    {
        if (Bukkit.getServer().getOfflinePlayer(str).getUniqueId() != null) {
            return Bukkit.getServer().getOfflinePlayer(str).getUniqueId();
        }
        return null;
    }

    public static UUID convertStringUUIDToUUID(String str)
    {
        if (UUID.fromString(str) != null) {
            return UUID.fromString(str);
        }
        return null;
    }
}
