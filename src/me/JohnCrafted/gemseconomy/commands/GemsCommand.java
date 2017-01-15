package me.JohnCrafted.gemseconomy.commands;

import me.JohnCrafted.gemseconomy.economy.GemMethods;
import me.JohnCrafted.gemseconomy.mysql.SQL;
import me.JohnCrafted.gemseconomy.utils.ConfigManager;
import me.JohnCrafted.gemseconomy.utils.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * Created by John on 09.04.2016.
 */
public class GemsCommand implements CommandExecutor
{
    GemMethods methods = new GemMethods();
    ConfigManager msg = ConfigManager.getConfigManager();

    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("gems"))
        {
            if (args.length == 0)
            {
                if (sender.hasPermission("gemseconomy.command.gems"))
                {
                    sender.sendMessage(ChatColor.GREEN + "§oGemsEconomy made by JohnCrafted");
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + "/balance (user)");
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + "/pay <user> <amount>");
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + "/gems add <user> <amount>");
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + "/gems take <user> <amount>");
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + "/gems set <user> <amount>");
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + "/gems reset <user>");
                    return true;
                }
                sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.noPermission"));
            }
            if (sender.hasPermission("gemseconomy.command.manage"))
            {
                if (args.length == 3)
                {
                    if (args[0].equalsIgnoreCase("add"))
                    {
                        String user = args[1];
                        String amount = args[2];
                        update(sender, user, EconomyAction.ADD, amount);
                    }
                    if (args[0].equalsIgnoreCase("take"))
                    {
                        String user = args[1];
                        String amount = args[2];
                        update(sender, user, EconomyAction.TAKE, amount);
                    }
                    if (args[0].equalsIgnoreCase("set"))
                    {
                        String user = args[1];
                        String amount = args[2];
                        update(sender, user, EconomyAction.SET, amount);
                    }
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("reset"))
                    {
                        String user = args[1];
                        update(sender, user, EconomyAction.RESET, "0");
                    }
                }
            }
        }
        return true;
    }

    public void update(CommandSender sender, String name, EconomyAction type, String strint)
    {
        if (UtilMath.isInteger(strint))
        {
            int i = Integer.parseInt(strint);
            UUID uuid = Bukkit.getServer().getPlayer(name).getUniqueId();
            if (uuid != null)
            {
                if (!this.methods.hasGems(uuid)) {
                    this.methods.registerUser(uuid);
                }
                if ((!SQL.isEnabled()) && (!SQL.isRegistered(uuid))) {
                    SQL.registerPlayer(uuid);
                }

                if (Bukkit.getPlayerExact(name) != null)
                {
                    if (type == EconomyAction.ADD)
                    {
                        this.methods.addGems(uuid, i);
                        Bukkit.getPlayerExact(name).sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsAdded").replace("%amount%", String.valueOf(i)).replace('&', '§'));
                        sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsAddedOther").replace("%amount%", String.valueOf(i)).replace("%player%", name).replace('&', '§'));
                    }
                    else if (type == EconomyAction.TAKE)
                    {
                        this.methods.takeGems(uuid, i);
                        Bukkit.getPlayerExact(name).sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsTaken").replace("%amount%", String.valueOf(i)).replace('&', '§'));
                        sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsTakenOther").replace("%amount%", String.valueOf(i)).replace("%player%", name).replace('&', '§'));
                    }
                    else if (type == EconomyAction.SET)
                    {
                        this.methods.setGems(uuid, i);
                        Bukkit.getPlayerExact(name).sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsSet").replace("%amount%", String.valueOf(i)).replace('&', '§'));
                        sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsSetOther").replace("%amount%", String.valueOf(i)).replace("%player%", name).replace('&', '§'));
                    }
                    else if (type == EconomyAction.RESET)
                    {
                        this.methods.resetGems(uuid);
                        Bukkit.getPlayerExact(name).sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsReset"));
                        sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsResetOther").replace("%player%", name).replace('&', '§'));
                    }
                }
                else if (type == EconomyAction.ADD)
                {
                    this.methods.addGems(uuid, i);
                    Bukkit.getPlayerExact(name).sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsAdded").replace("%amount%", String.valueOf(i)).replace('&', '§'));
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsAddedOther").replace("%amount%", String.valueOf(i)).replace("%player%", name).replace('&', '§'));
                }
                else if (type == EconomyAction.TAKE)
                {
                    this.methods.takeGems(uuid, i);
                    Bukkit.getPlayerExact(name).sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsTaken").replace("%amount%", String.valueOf(i)).replace('&', '§'));
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsTakenOther").replace("%amount%", String.valueOf(i)).replace("%player%", name).replace('&', '§'));
                }
                else if (type == EconomyAction.SET)
                {
                    this.methods.setGems(uuid, i);
                    Bukkit.getPlayerExact(name).sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsSet").replace("%amount%", String.valueOf(i)).replace('&', '§'));
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsSetOther").replace("%amount%", String.valueOf(i)).replace("%player%", name).replace('&', '§'));
                }
                else if (type == EconomyAction.RESET)
                {
                    this.methods.resetGems(uuid);
                    Bukkit.getPlayerExact(name).sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsReset"));
                    sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.gemsResetOther").replace("%player%", name).replace('&', '§'));
                }
            }
            else
            {
                sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.notAValidUser"));
            }
        }
        else
        {
            sender.sendMessage(msg.getConfig().getString("messages.prefix").replace('&', '§') + msg.getConfig().getString("messages.txtColor").replace('&', '§') + msg.getConfig().getString("messages.notAValidInt"));
        }
    }
}
