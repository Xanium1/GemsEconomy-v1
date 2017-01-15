package me.JohnCrafted.gemseconomy.economy;

import me.JohnCrafted.gemseconomy.mysql.SQL;
import me.JohnCrafted.gemseconomy.utils.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static me.JohnCrafted.gemseconomy.GemsEconomy.plugin;

/**
 * Created by John on 09.04.2016.
 */
public class GemMethods {

    private ConfigManager cm = ConfigManager.getConfigManager();

    public int getGems(UUID uuid) {
        if (SQL.isEnabled()) {
            return SQL.getGems(uuid);
        }
        return this.cm.getConfig().getInt("gems." + uuid.toString());
    }

    public boolean hasGems(UUID uuid) {
        if (SQL.isEnabled()) {
            if(SQL.hasGems(uuid)){
                return true;
            }
            return false;
        }else if(cm.getConfig().contains("gems." + uuid.toString())){
            return true;
        }
        return false;
    }

    public void unsetGems(UUID uuid)
    {
        if (hasGems(uuid))
        {
            this.cm.getConfig().set("gems." + uuid.toString(), null);
            this.cm.saveConfig();
            reload();
        }
    }

    public int setGems(UUID uuid, int i)
    {
        new BukkitRunnable() {

            public void run() {

                if (SQL.isEnabled()) {
                    SQL.setGems(uuid, i);
                } else {
                    unsetGems(uuid);
                    cm.getConfig().set("gems." + uuid.toString(), i);
                    cm.saveConfig();
                    reload();

                }
            }
        }.runTaskAsynchronously(plugin);
        return i;
    }

    public void resetGems(UUID uuid)
    {
        if (SQL.isEnabled()) {
            SQL.setGems(uuid, 0);
        }
        cm.getConfig().set("gems." + uuid.toString(), 0);
        cm.saveConfig();
        reload();
    }

    public void addGems(UUID uuid, int amount)
    {
        setGems(uuid, getGems(uuid) + amount);
    }

    public void takeGems(UUID uuid, int amount)
    {
        if (getGems(uuid) <= 0) {
            resetGems(uuid);
            return;
        }
        setGems(uuid, getGems(uuid) - amount);
    }

    public void registerUser(UUID uuid)
    {
        setGems(uuid, 0);
    }

    public void reload()
    {
        cm.saveConfig();
    }
}
