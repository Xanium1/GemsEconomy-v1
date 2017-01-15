package me.JohnCrafted.gemseconomy.pluginhooks;

import me.JohnCrafted.gemseconomy.GemsEconomy;
import me.JohnCrafted.gemseconomy.economy.GemMethods;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

/**
 * Created by John on 09.04.2016.
 */
public class ClipsPlaceholderAPI extends EZPlaceholderHook
{
    private GemsEconomy plugin;
    private GemMethods methods;

    public ClipsPlaceholderAPI(GemsEconomy plugin, GemMethods methods)
    {
        super(plugin, "gemsEconomy");

        this.methods = methods;
        this.plugin = plugin;
    }

    public String onPlaceholderRequest(Player player, String s)
    {
        if (s.equals("gems")) {
            return String.valueOf(this.methods.getGems(player.getUniqueId()));
        }
        if (player == null) {
            return "";
        }
        return null;
    }
}
