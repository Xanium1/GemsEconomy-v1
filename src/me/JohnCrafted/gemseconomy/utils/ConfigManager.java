package me.JohnCrafted.gemseconomy.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by John on 09.04.2016.
 */
public class ConfigManager {

    public static ConfigManager getConfigManager()
    {
        return instance;
    }

    static ConfigManager instance = new ConfigManager();
    private ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    FileConfiguration config;
    File cfile;

    public void setupConfig(Plugin plugin)
    {
        this.config = plugin.getConfig();
        this.config.options().copyDefaults(true);
        this.cfile = new File(plugin.getDataFolder(), "config.yml");
        saveConfig();
    }

    public void saveConfig()
    {
        try
        {
            this.config.save(this.cfile);
        }
        catch (IOException e)
        {
            ccs.sendMessage("§c[GemsEconomy] Could not save config.yml!");
            e.getCause().printStackTrace();
        }
    }

    public void reloadConfig()
    {
        this.config = YamlConfiguration.loadConfiguration(this.cfile);
    }

    public FileConfiguration getConfig()
    {
        return this.config;
    }

}
