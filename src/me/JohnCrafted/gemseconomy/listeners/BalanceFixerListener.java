package me.JohnCrafted.gemseconomy.listeners;

import me.JohnCrafted.gemseconomy.economy.GemMethods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by Jon Kristoffer on 11.09.2016.
 */
public class BalanceFixerListener implements Listener {

    GemMethods gm = new GemMethods();

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if(gm.getGems(p.getUniqueId()) < 0){
            gm.setGems(p.getUniqueId(), 0);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if(gm.getGems(p.getUniqueId()) < 0){
            gm.setGems(p.getUniqueId(), 0);
        }
    }
}
