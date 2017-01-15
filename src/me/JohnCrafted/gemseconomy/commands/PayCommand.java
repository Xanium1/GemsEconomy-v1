package me.JohnCrafted.gemseconomy.commands;

import me.JohnCrafted.gemseconomy.economy.GemMethods;
import me.JohnCrafted.gemseconomy.mysql.SQL;
import me.JohnCrafted.gemseconomy.utils.ConfigManager;
import me.JohnCrafted.gemseconomy.utils.UUIDConverter;
import me.JohnCrafted.gemseconomy.utils.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Jon Kristoffer on 11.09.2016.
 */
public class PayCommand implements CommandExecutor {

    ConfigManager cm = ConfigManager.getConfigManager();
    GemMethods gem = new GemMethods();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.notAPlayer"));
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("gemseconomy.command.pay")){
            p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.noPermission"));
            return true;
        }
        if(args.length == 0){
            p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.payUsage").replace('&', '§'));
            return true;
        }
        if(args.length == 1){
            p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.payUsage").replace('&', '§'));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){
            p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.playerNotFound").replace("%player%", target.getName()).replace('&', '§'));
            return true;
        }
        if(args.length == 2){

            if(gem.getGems(p.getUniqueId()) <= 0) {
                p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.notEnoughGems"));
                return true;
            }else{
                update(p, target.getName(), EconomyAction.PAY, args[1]);
                target.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.gemsPaidOther").replace("%amount%", String.valueOf(args[1])).replace("%player%", sender.getName()).replace('&', '§'));
                p.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.gemsPaid").replace("%amount%", String.valueOf(args[1])).replace("%player%", target.getName()).replace('&', '§'));
            }
        }
        return true;
    }

    public void update(CommandSender sender, String name, EconomyAction type, String strint) {
        if (UtilMath.isInteger(strint)) {
            int i = Integer.parseInt(strint);
            UUID uuid = Bukkit.getServer().getPlayer(name).getUniqueId();
            if (uuid != null) {

                if (!this.gem.hasGems(uuid)) {
                    this.gem.registerUser(uuid);
                }
                if (!this.gem.hasGems(UUIDConverter.convertUsernameToUUID(sender.getName()))) {
                    this.gem.registerUser(UUIDConverter.convertUsernameToUUID(sender.getName()));
                }
                if ((!SQL.isEnabled()) && (!SQL.isRegistered(uuid))) {
                    SQL.registerPlayer(uuid);
                }
                if ((!SQL.isEnabled()) && (!SQL.isRegistered(UUIDConverter.convertUsernameToUUID(sender.getName())))) {
                    SQL.registerPlayer(UUIDConverter.convertUsernameToUUID(sender.getName()));
                }

                if (Bukkit.getPlayerExact(name) != null) {

                    if (type == EconomyAction.PAY) {
                        if(gem.hasGems(UUIDConverter.convertUsernameToUUID(sender.getName()))) {
                            gem.takeGems(UUIDConverter.convertUsernameToUUID(sender.getName()), i);
                            gem.addGems(uuid, i);
                        }else{
                            sender.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.notEnoughGems"));
                        }
                    }
                } else {
                    sender.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.notAValidUser"));
                }
            } else {
                sender.sendMessage(cm.getConfig().getString("messages.prefix").replace('&', '§') + cm.getConfig().getString("messages.txtColor").replace('&', '§') + cm.getConfig().getString("messages.notAValidInt"));
            }
        }
    }
}
