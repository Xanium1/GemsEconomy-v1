package me.JohnCrafted.gemseconomy.mysql;

import me.JohnCrafted.gemseconomy.utils.ConfigManager;
import me.JohnCrafted.gemseconomy.utils.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

/**
 * Created by Jon Kristoffer on 11.09.2016.
 */
public class SQL {

    private static ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private static Connection c;
    static ConfigManager cm = ConfigManager.getConfigManager();

    public static Connection getConnection(){
        return c;
    }

    public static String getUsername(){
        return cm.getConfig().getString("MySQL.user");
    }

    public static String getPassword(){
        return cm.getConfig().getString("MySQL.password");
    }

    public static int getPort(){
        if(UtilMath.isInteger(cm.getConfig().getString("MySQL.port"))){
            return Integer.parseInt(cm.getConfig().getString("MySQL.port"));
        }
        return 3306;
    }

    public static String getDatabase(){
        return cm.getConfig().getString("MySQL.database");
    }

    public static String getHost(){
        return cm.getConfig().getString("MySQL.host");
    }

    public static boolean isEnabled(){
        return cm.getConfig().getBoolean("MySQL.enable");
    }

    public static String getTablePrefix(){
        return cm.getConfig().getString("MySQL.table");
    }

    public static void openConnection() {
        if (isEnabled()) {
            try
            {
                c = DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase(), getUsername(), getPassword());
            }
            catch (Exception e)
            {
                ccs.sendMessage("§c[GemsEconomy] Could not connect to the MySQL Database. Removing cache...");
                generateException(e);
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.kickPlayer("[GemsEconomy] Could not connect to the database. In order to prevent glitches, you have been kicked.");
                }
            }
        }
    }

    public static void closeConnection() {
        if (isEnabled()) {
            try
            {
                if (!c.isClosed()) {
                    c.close();
                }
            }
            catch (Exception e)
            {
                generateException(e);
            }
        }
    }

    public static void createDefaultTable() {
        if (isEnabled())
        {
            openConnection();
            String query = "CREATE TABLE IF NOT EXISTS `" + getTablePrefix() + "gems` (`id` bigint(11) AUTO_INCREMENT, `uuid` varchar(255) NOT NULL, `balance` int(11), PRIMARY KEY (`id`) );";
            try
            {
                PreparedStatement pre = c.prepareStatement(query);
                pre.execute();
                pre.close();
            }
            catch (Exception e)
            {
                generateException(e);
            }
            closeConnection();
        }
    }

    public static void generateException(Exception e) {
        System.out.println("[GemsEconomy] There was an error in MySQL. Java error: " + e.getMessage());
    }

    public static void generateException(String str) {
        System.out.println("[GemsEconomy] There was an error in MySQL. Here is the error: " + str);
    }


    public static boolean isRegistered(UUID uuid)
    {
        if (isEnabled())
        {
            openConnection();
            String query = "SELECT COUNT(*) FROM " + getTablePrefix() + "gems WHERE uuid='" + uuid.toString() + "' LIMIT 1";
            try
            {
                PreparedStatement pre = c.prepareStatement(query);
                ResultSet set = pre.executeQuery();
                set.next();
                int i = set.getInt(1);
                return i == 1;
            }
            catch (Exception e)
            {
                generateException(e);
                closeConnection();
                return false;
            }
        }
        return false;
    }

    public static void registerPlayer(UUID uuid)
    {
        if (isEnabled())
        {
            if (isRegistered(uuid)) {
                return;
            }
            openConnection();
            String query = "INSERT INTO " + getTablePrefix() + "gems (uuid, balance) VALUES ('" + uuid.toString() + "', '0')";
            try
            {
                PreparedStatement pre = c.prepareStatement(query);
                pre.execute();
                pre.close();
            }
            catch (Exception e)
            {
                generateException(e);
            }
            closeConnection();
        }
    }

    public static int getGems(UUID uuid)
    {
        if (isEnabled())
        {
            openConnection();
            String query = "SELECT balance FROM " + getTablePrefix() + "gems WHERE uuid='" + uuid.toString() + "' LIMIT 1";
            try
            {
                PreparedStatement pre = c.prepareStatement(query);
                ResultSet set = pre.executeQuery();
                set.next();
                return set.getInt(1);
            }
            catch (Exception e)
            {
                generateException(e);
                closeConnection();
                return -1;
            }
        }
        return -1;
    }

    public static boolean hasGems(UUID uuid)
    {
        if (isEnabled())
        {
            openConnection();
            String query = "SELECT balance FROM " + getTablePrefix() + "gems WHERE uuid='" + uuid.toString() + "' LIMIT 1";
            try
            {
                PreparedStatement pre = c.prepareStatement(query);
                ResultSet set = pre.executeQuery();
                set.next();
                return true;
            }
            catch (Exception e)
            {
                generateException(e);
                closeConnection();
                return false;
            }
        }
        return false;
    }

    public static void setGems(UUID uuid, int i)
    {
        if (isEnabled())
        {
            if (!isRegistered(uuid)) {
                registerPlayer(uuid);
            }
            openConnection();
            String query = "UPDATE " + getTablePrefix() + "gems SET balance='" + i + "' WHERE uuid='" + uuid.toString() + "'";
            try
            {
                PreparedStatement pre = c.prepareStatement(query);
                pre.execute();
                pre.close();
            }
            catch (Exception e)
            {
                generateException(e);
            }
            closeConnection();
        }
    }

    public static void addGems(UUID uuid, int i)
    {
        setGems(uuid, getGems(uuid) + i);
    }

    public static void removeGems(UUID uuid, int i)
    {
        setGems(uuid, getGems(uuid) - i);
    }
}
