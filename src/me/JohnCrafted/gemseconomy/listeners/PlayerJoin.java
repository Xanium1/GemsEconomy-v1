package me.JohnCrafted.gemseconomy.listeners;

import me.JohnCrafted.gemseconomy.economy.GemMethods;
import me.JohnCrafted.gemseconomy.mysql.SQL;
import me.JohnCrafted.gemseconomy.utils.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * Created by John on 09.04.2016.
 */
public class PlayerJoin implements Listener
{
    GemMethods methods = new GemMethods();
    ConfigManager msg = ConfigManager.getConfigManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player p = event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();
        if (SQL.isEnabled())
        {
            if (!SQL.isRegistered(p.getUniqueId())) {
                SQL.registerPlayer(p.getUniqueId());
                p.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsAccountCreated"));
            }
        }
        else if (!this.methods.hasGems(uuid))
        {
            this.methods.registerUser(uuid);
            p.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsAccountCreated"));
            return;
        }
    }
}
