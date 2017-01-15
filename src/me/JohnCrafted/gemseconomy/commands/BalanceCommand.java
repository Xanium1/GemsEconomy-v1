package me.JohnCrafted.gemseconomy.commands;

import me.JohnCrafted.gemseconomy.economy.GemMethods;
import me.JohnCrafted.gemseconomy.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jon Kristoffer on 11.09.2016.
 */
public class BalanceCommand implements CommandExecutor {

    ConfigManager cm = ConfigManager.getConfigManager();
    GemMethods gem = new GemMethods();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.notAPlayer"));
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("gemseconomy.command.balance")){
            p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.noPermission"));
            return true;
        }
        if(args.length == 0){
            p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.balance").replace("%balance%", String.valueOf(gem.getGems(p.getUniqueId()))).replace('&', '§'));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){

            return true;
        }
        if(args.length == 1){
            if(p.hasPermission("gemseconomy.command.balance.other")){
                sender.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.balanceOther").replace("%balance%", String.valueOf(gem.getGems(target.getUniqueId()))).replace('&', '§').replace("%player%", target.getName()));
                return true;
            }else{
                p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.noPermission"));
            }
        }
        return true;
    }
}
