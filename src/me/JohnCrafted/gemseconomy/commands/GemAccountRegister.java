package me.JohnCrafted.gemseconomy.commands;

import me.JohnCrafted.gemseconomy.economy.GemMethods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 09.04.2016.
 */
public class GemAccountRegister implements CommandExecutor
{
    private GemMethods methods;

    public GemAccountRegister(GemMethods methods)
    {
        this.methods = methods;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (!(commandSender instanceof Player))
        {
            commandSender.sendMessage("§a§lGems §7You are not a player.");
            return true;
        }
        Player p = (Player)commandSender;
        if (!p.hasPermission("gemseconomy.command.gemacc"))
        {
            p.sendMessage("§a§lGems §7You do not have the required permission to execute this command or action.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("gemacc"))
        {
            if (args.length == 0)
            {
                p.sendMessage("§a§lGems §7/gemacc register - Register an account for you.");
                p.sendMessage("§a§lGems §7/gemacc delete - Deletes your account and gems.");
            }
            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("register")) {
                    if (!this.methods.hasGems(p.getUniqueId()))
                    {
                        this.methods.registerUser(p.getUniqueId());
                        p.sendMessage("§a§lGems §7You have registered your gem account.");
                    }
                    else
                    {
                        p.sendMessage("§a§lGems §7You have already registered an account on your name.");
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (this.methods.hasGems(p.getUniqueId()))
                    {
                        this.methods.unsetGems(p.getUniqueId());
                        p.sendMessage("§a§lGems §7You have deleted your gem account.");
                    }
                    else
                    {
                        p.sendMessage("§a§lGems §7You don't have a gem account.");
                    }
                }
            }
        }
        return true;
    }
}
