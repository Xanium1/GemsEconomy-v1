package me.JohnCrafted.gemseconomy;

import me.JohnCrafted.gemseconomy.commands.BalanceCommand;
import me.JohnCrafted.gemseconomy.commands.GemsCommand;
import me.JohnCrafted.gemseconomy.commands.PayCommand;
import me.JohnCrafted.gemseconomy.economy.GemMethods;
import me.JohnCrafted.gemseconomy.listeners.BalanceFixerListener;
import me.JohnCrafted.gemseconomy.listeners.PlayerJoin;
import me.JohnCrafted.gemseconomy.mysql.SQL;
import me.JohnCrafted.gemseconomy.pluginhooks.ClipsPlaceholderAPI;
import me.JohnCrafted.gemseconomy.pluginhooks.MVdWPlaceholderAPI;
import me.JohnCrafted.gemseconomy.utils.ConfigManager;
import me.JohnCrafted.gemseconomy.utils.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by John on 09.04.2016.
 */
public class GemsEconomy extends JavaPlugin
{
    ConfigManager config = ConfigManager.getConfigManager();
    private ConsoleCommandSender cs = Bukkit.getConsoleSender();
    private GemMethods methods;
    private ClipsPlaceholderAPI cpapi;
    private MVdWPlaceholderAPI mvdwpapi;
    public static GemsEconomy plugin;

    @Override
    public void onEnable()
    {
        long startupTime = System.currentTimeMillis();
        plugin = this;
        cs.sendMessage("§a[GemsEconomy] §7Enabling..");
        this.config.setupConfig(this);
        this.methods = new GemMethods();

        if(SQL.isEnabled()){

            cs.sendMessage("§b[GemsEconomy] §7Connecting to MySQL..");
            SQL.createDefaultTable();
            cs.sendMessage("§b[GemsEconomy] §7Connected.");
            cs.sendMessage("§b[GemsEconomy] §7Storge: SQL");
        }else {
            cs.sendMessage("§b[GemsEconomy] §7Storage: Accounts.yml");
        }

        registerCommands();
        getServer().getPluginManager().registerEvents(new BalanceFixerListener(), this);
        if (this.config.getConfig().getBoolean("settings.mvdwplaceholderapi"))
        {
            this.mvdwpapi = new MVdWPlaceholderAPI(this, this.methods, this.config);
            this.mvdwpapi.MVdWPlaceholderHook();
        }
        if (this.config.getConfig().getBoolean("settings.ezplaceholderapi"))
        {
            this.cpapi = new ClipsPlaceholderAPI(this, this.methods);
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                this.cpapi.hook();
            }
        }
        if(this.config.getConfig().getBoolean("settings.registerPlayerOnJoin")){
            registerListeners();
        }
        cs.sendMessage("§a[GemsEconomy] §7Enabled in " + UtilTime.convertString(System.currentTimeMillis() - startupTime, 4, UtilTime.TimeUnit.FIT) + ".");
    }
    @Override
    public void onDisable() {
        this.config.reloadConfig();
        this.config.saveConfig();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerJoin(), this);
    }

    private void registerCommands(){
        getCommand("gems").setExecutor(new GemsCommand());
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("pay").setExecutor(new PayCommand());
    }
}
