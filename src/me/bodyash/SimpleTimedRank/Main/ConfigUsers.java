package me.bodyash.SimpleTimedRank.Main;

import me.bodyash.SimpleTimedRank.Main.Main;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public class ConfigUsers {
    private Main main;
    private String consoleLogo;
    private File configFile;
    private YamlConfiguration config;
    private double versionNumberStorage;
    public double versionNumber;
    private String versionNumberPath;

    public ConfigUsers(Main main) {
        this.versionNumber = this.versionNumberStorage = 1.1;
        this.versionNumberPath = "VersionNumber";
        this.main = main;
        this.consoleLogo = main.getConsoleLogo();
        this.configFile = new File(main.getDataFolder(), "users.yml");
        this.config = YamlConfiguration.loadConfiguration((File)this.configFile);
        this.loadUsersConfig();
    }

    private void loadUsersConfig() {
        if (!this.configFile.exists()) {
            System.out.println(String.valueOf(this.consoleLogo) + "... Starting users config creation ...");
            this.createUsersConfig();
        } else if (Double.valueOf(this.config.getString(this.versionNumberPath)) < this.versionNumber) {
            System.err.println(String.valueOf(this.consoleLogo) + "... New users config version detected ...");
            System.err.println(String.valueOf(this.consoleLogo) + "... Backing up old users config ...");
            System.err.println(String.valueOf(this.consoleLogo) + "... Don't forget to restore your user data! ...");
            System.err.println(String.valueOf(this.consoleLogo) + "... Don't forget to restore your user data! ...");
            System.err.println(String.valueOf(this.consoleLogo) + "... Don't forget to restore your user data! ...");
            System.err.println(String.valueOf(this.consoleLogo) + "... Sorry for that but if I change something in the users.yml this should be very important! ...");
            try {
                this.configFile.renameTo(new File(this.main.getDataFolder(), "usersOld.yml"));
                File configFile2 = new File(this.main.getDataFolder(), "users.yml");
                if (configFile2.exists()) {
                    configFile2.delete();
                }
                this.createUsersConfig();
            }
            catch (Exception e) {
                System.err.println(String.valueOf(this.consoleLogo) + "Can't backup the OLD users config file, see info below:");
                e.printStackTrace();
            }
            if (!this.configFile.exists()) {
                System.out.println(String.valueOf(this.consoleLogo) + "... Starting NEW users config creation ...");
                this.createUsersConfig();
                try {
                    this.config.save(this.configFile);
                    System.out.println(String.valueOf(this.consoleLogo) + "... Finished NEW users config creation!");
                }
                catch (IOException e) {
                    System.err.println(String.valueOf(this.consoleLogo) + "Can't create the NEW users config file, see info below:");
                    e.printStackTrace();
                }
            }
        }
    }

    private void createUsersConfig() {
        this.config.options().header("Info:\n!!ATTENTION: Please do not touch the version number!!\nStatus declaration:\n1 --> this means the status is active\n0 --> currently unused\n-1 --> the status is expired \n!!ATTENTION: Do not touch the user data only if you know what you do!!");
        this.config.set(this.versionNumberPath, (Object)this.versionNumber);
        this.config.createSection("Users");
        try {
            this.config.save(this.configFile);
            System.out.println(String.valueOf(this.consoleLogo) + "... Finished users config creation!");
        }
        catch (IOException e) {
            System.err.println(String.valueOf(this.consoleLogo) + "Can't create users config file, see info below:");
            e.printStackTrace();
        }
    }

    public void addUser(String name, String promotedRank, String untilDate, String untilTime, String fromDate, String fromTime, String oldRank) {
        try {
            this.config.set("Users." + name + ".UntilDate", (Object)untilDate);
            this.config.set("Users." + name + ".UntilTime", (Object)untilTime);
            this.config.set("Users." + name + ".FromDate", (Object)fromDate);
            this.config.set("Users." + name + ".FromTime", (Object)fromTime);
            this.config.set("Users." + name + ".PromotedRank", (Object)promotedRank);
            this.config.set("Users." + name + ".OldRank", (Object)oldRank);
            this.config.set("Users." + name + ".Status", (Object)"1");
            this.config.save(this.configFile);
        }
        catch (Exception e) {
            System.err.println(String.valueOf(this.consoleLogo) + "An error occured while trying to add a new player to the users config!");
        }
    }

    public String getUserData(String playerName, String whichData) {
        try {
            File configFile = new File(this.main.getDataFolder(), "users.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration((File)configFile);
            if (whichData.equalsIgnoreCase("oldRank")) {
                return config.getString("Users." + playerName + ".OldRank");
            }
            if (whichData.equalsIgnoreCase("untilDate")) {
                return config.getString("Users." + playerName + ".UntilDate");
            }
            if (whichData.equalsIgnoreCase("untilTime")) {
                return config.getString("Users." + playerName + ".UntilTime");
            }
            if (whichData.equalsIgnoreCase("fromDate")) {
                return config.getString("Users." + playerName + ".FromDate");
            }
            if (whichData.equalsIgnoreCase("fromTime")) {
                return config.getString("Users." + playerName + ".FromTime");
            }
            if (whichData.equalsIgnoreCase("promotedRank")) {
                return config.getString("Users." + playerName + ".PromotedRank");
            }
            if (whichData.equalsIgnoreCase("status")) {
                return config.getString("Users." + playerName + ".Status");
            }
        }
        catch (Exception e) {
            return null;
        }
        return null;
    }

    public void setUserTimeExpired(String name) {
        try {
            String expired = "expired";
            this.config.set("Users." + name + ".UntilDate", (Object)expired);
            this.config.set("Users." + name + ".UntilTime", (Object)expired);
            this.config.set("Users." + name + ".FromDate", (Object)expired);
            this.config.set("Users." + name + ".FromTime", (Object)expired);
            this.config.set("Users." + name + ".PromotedRank", (Object)expired);
            this.config.set("Users." + name + ".OldRank", (Object)expired);
            this.config.set("Users." + name + ".Status", (Object)"-1");
            this.config.save(this.configFile);
        }
        catch (Exception e) {
            System.err.println(String.valueOf(this.consoleLogo) + "An error occured while trying to expire the time of the player " + name + "!");
            System.err.println(String.valueOf(this.consoleLogo) + "If you know that this player exist please do this manually; Sorry for that!");
        }
    }
}

