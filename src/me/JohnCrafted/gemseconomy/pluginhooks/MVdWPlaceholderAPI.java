package me.JohnCrafted.gemseconomy.pluginhooks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.JohnCrafted.gemseconomy.GemsEconomy;
import me.JohnCrafted.gemseconomy.economy.GemMethods;
import me.JohnCrafted.gemseconomy.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by John on 09.04.2016.
 */
public class MVdWPlaceholderAPI {

    private GemsEconomy plugin;
    private GemMethods methods;
    private ConfigManager config;

    public MVdWPlaceholderAPI(GemsEconomy plugin, GemMethods methods, ConfigManager config)
    {
        this.plugin = plugin;
        this.methods = methods;
        this.config = config;
    }

    public void MVdWPlaceholderHook()
    {
        if (this.config.getConfig().getBoolean("settings.mvdwplaceholderapi"))
        {
            if (Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI").isEnabled()) {
                PlaceholderAPI.registerPlaceholder(this.plugin, "gems", new PlaceholderReplacer()
                {
                    public String onPlaceholderReplace(PlaceholderReplaceEvent event)
                    {
                        Player player = event.getPlayer();
                        UUID id = event.getPlayer().getUniqueId();
                        if (player == null) {
                            return "Player needed!";
                        }
                        return String.valueOf(MVdWPlaceholderAPI.this.methods.getGems(id));
                    }
                });
            } else {
                System.out.println("[GemsEconomy] MVdWPlaceholderAPI not found, disabling placeholders.");
            }
        }
        else {
            System.out.println("[GemsEconomy] MVdWPlaceholderAPI not enabled, disabling placeholders.");
        }
    }
}
