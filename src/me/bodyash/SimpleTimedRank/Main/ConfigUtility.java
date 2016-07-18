
package me.bodyash.SimpleTimedRank.Main;

import me.bodyash.SimpleTimedRank.Main.Main;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public class ConfigUtility {
    private Main main;
    private String consoleLogo;
    private File configFile;
    private YamlConfiguration config;
    private double versionNumberStorage = 1.2;
    private String messages = "Messages.";
    public String noPermMessage = "&3You don't have permissions to do that!";
    private String noPermMessagePath = String.valueOf(this.messages) + "NoPermMessage";
    public String lastDayMsg = "This is your last day as %oldRank%!";
    private String lastDayMsgPath = String.valueOf(this.messages) + "LastDayMessage";
    public String timeExpiredMsg = "Your time has been expired!";
    private String timeExpiredMsgPath = String.valueOf(this.messages) + "TimeExpiredMessage";
    public String cantCheckTimeMsg = "Can't check the time because you are not registered yet.";
    private String cantCheckTimeMsgPath = String.valueOf(this.messages) + "CantCheckTimeMessage";
    private String options = "Options.";
    public String checkMethod = "onPlayerJoin";
    private String checkMethodPath = String.valueOf(this.options) + "CheckMethod";
    public boolean checkVersion = true;
    private String checkVersionPath = String.valueOf(this.options) + "CheckVersion";
    public long interval = 5;
    private String intervalPath = String.valueOf(this.options) + "Interval";
    public String dateFormat = "dd.MM.yyyy";
    private String dateFormatPath = String.valueOf(this.options) + "DateFormat";
    public String promoteCommand = "pex user %player% group set %newRank%";
    private String promoteCommandPath = String.valueOf(this.options) + "PromoteCommand";
    public String demoteCommand = "pex user %player% group set %oldRank%";
    private String demoteCommandPath = String.valueOf(this.options) + "DemoteCommand";
    public double versionNumber = this.versionNumberStorage;
    private String versionNumberPath = "VersionNumber";

    public ConfigUtility(Main main) {
        this.main = main;
        this.consoleLogo = main.getConsoleLogo();
        this.configFile = new File(main.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration((File)this.configFile);
        this.loadUsersConfig();
    }

    private void loadUsersConfig() {
        if (!this.configFile.exists()) {
            System.out.println(String.valueOf(this.consoleLogo) + "... Starting config creation ...");
            this.createConfig();
        } else if (Double.valueOf(this.config.getString(this.versionNumberPath)) < this.versionNumber) {
            System.err.println(String.valueOf(this.consoleLogo) + "... New config version detected ...");
            System.err.println(String.valueOf(this.consoleLogo) + "... Backing up old config ...");
            try {
                this.configFile.renameTo(new File(this.main.getDataFolder(), "configOld.yml"));
                File configFile2 = new File(this.main.getDataFolder(), "config.yml");
                if (configFile2.exists()) {
                    configFile2.delete();
                }
                this.createConfig();
            }
            catch (Exception e) {
                System.err.println(String.valueOf(this.consoleLogo) + "Can't backup the OLD config file, see info below:");
                e.printStackTrace();
            }
            if (!this.configFile.exists()) {
                System.out.println(String.valueOf(this.consoleLogo) + "... Starting NEW config creation ...");
                this.createConfig();
                try {
                    this.config.save(this.configFile);
                    System.out.println(String.valueOf(this.consoleLogo) + "... Finished NEW config creation!");
                }
                catch (IOException e) {
                    System.err.println(String.valueOf(this.consoleLogo) + "Can't create the NEW config file, see info below:");
                    e.printStackTrace();
                }
            }
        } else {
            block31 : {
                if (this.config.getString(this.noPermMessagePath).isEmpty()) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"NoPermMessage\", using default message (\u00a73You don't have permissions to do that!). ...");
                    this.noPermMessage = "&3You don't have permissions to do that!";
                } else {
                    this.noPermMessage = this.config.getString(this.noPermMessagePath);
                }
                if (this.config.getString(this.dateFormatPath).isEmpty()) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"DateFormat\", using default date format (dd.MM.yyyy). ...");
                    this.dateFormat = "dd.MM.yyyy";
                } else {
                    this.dateFormat = this.config.getString(this.dateFormatPath);
                }
                if (this.config.getString(this.promoteCommandPath).isEmpty()) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"PromoteCommand\", using default command (pex user %player% group set %newRank%). ...");
                    this.promoteCommand = "pex user %player% group set %newRank%";
                } else {
                    this.promoteCommand = this.config.getString(this.promoteCommandPath);
                }
                if (this.config.getString(this.demoteCommandPath).isEmpty()) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"DemoteCommand\", using default command (pex user %player% group set %oldRank%). ...");
                    this.demoteCommand = "pex user %player% group set %oldRank%";
                } else {
                    this.demoteCommand = this.config.getString(this.demoteCommandPath);
                }
                if (this.config.getString(this.lastDayMsgPath).isEmpty()) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"LastDayMessage\", using default message (This is your last day as %oldRank%!). ...");
                    this.lastDayMsg = "This is your last day as %oldRank%!";
                } else {
                    this.lastDayMsg = this.config.getString(this.lastDayMsgPath);
                }
                if (this.config.getString(this.timeExpiredMsgPath).isEmpty()) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"TimeExpiredMessage\", using default message (Your time has been expired!). ...");
                    this.timeExpiredMsg = "Your time has been expired!";
                } else {
                    this.timeExpiredMsg = this.config.getString(this.timeExpiredMsgPath);
                }
                if (this.config.getString(this.cantCheckTimeMsgPath).isEmpty()) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"CantCheckTimeMessage\", using default message (Can't check the time because you are not registered yet.). ...");
                    this.cantCheckTimeMsg = "Can't check the time because you are not registered yet.";
                } else {
                    this.cantCheckTimeMsg = this.config.getString(this.cantCheckTimeMsgPath);
                }
                if (this.config.getString(this.checkMethodPath).isEmpty() || !this.config.getString(this.checkMethodPath).equalsIgnoreCase("all") && !this.config.getString(this.checkMethodPath).equalsIgnoreCase("interval") && !this.config.getString(this.checkMethodPath).equalsIgnoreCase("onPlayerJoin")) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"CheckMethod\", using default setting (onPlayerJoin). ...");
                    this.checkMethod = "onPlayerJoin";
                } else {
                    this.checkMethod = this.config.getString(this.checkMethodPath);
                }
                try {
                    if (!this.config.getString(this.intervalPath).isEmpty()) {
                        long checkedLong;
                        this.interval = checkedLong = Long.parseLong(this.config.getString(this.intervalPath));
                        break block31;
                    }
                    throw new IllegalArgumentException();
                }
                catch (Exception e) {
                    System.err.println(String.valueOf(this.main.getConsoleLogo()) + "... Something went wrong while setting the \"Interval\", using default value (5 [Minutes]). ...");
                    this.interval = 5;
                }
            }
            if (this.config.getString(this.checkVersionPath).compareToIgnoreCase("true") != 0 && this.config.getString(this.checkVersionPath).compareToIgnoreCase("false") != 0) {
                System.err.println("... Something went wrong while setting the \"CheckVersion\", using default setting (true). ...");
                this.checkVersion = true;
            } else {
                this.checkVersion = Boolean.valueOf(this.config.getString(this.checkVersionPath));
            }
        }
    }

    private void createConfig() {
        this.config.options().header("Info:\nThis is a short config declaration if you need a detailed declaration of all methods visit: http://dev.bukkit.org/server-mods/highway/\nThe DateFormat is not really testet yet. If you are not sure what you do don't touch that in this version!\nIts not recommended to change this!\nDateFormat: dd.MM.yyyy\nIf you want to change something be sure to use the right case!\nFor example: MM.dd.yyyy\nAvailable variables: %player% ; %newRank% ; %oldRank%\nBut note: The variables are not working for the following messages / commands:\n    - NoPermMessage\n    - TimeExpired\n    - CantCheckTimeMessage\nChange the CheckMethod if you want the plugin to use another time checking method\nAvailable methods are:\n    - all\t\t\t\t\t: use all existing ckeck methods -> resource intensive if you set a low number of intervals.\n    - onPlayerJoin\t: everytime a player is joining, his/her time will be checked.\n    - interval\t\t\t: every x minutes the server will check ALL online players to promot or demote them if they exist in the users.yml.\nThe \"interval\" is defined in minutes. Please use whole numbers.\n!!ATTENTION: Please do not touch the version number!!");
        this.config.set(this.versionNumberPath, (Object)this.versionNumber);
        this.config.set(this.noPermMessagePath, (Object)this.noPermMessage);
        this.config.set(this.lastDayMsgPath, (Object)this.lastDayMsg);
        this.config.set(this.timeExpiredMsgPath, (Object)this.timeExpiredMsg);
        this.config.set(this.cantCheckTimeMsgPath, (Object)this.cantCheckTimeMsg);
        this.config.set(this.checkMethodPath, (Object)this.checkMethod);
        this.config.set(this.checkVersionPath, (Object)this.checkVersion);
        this.config.set(this.intervalPath, (Object)this.interval);
        this.config.set(this.dateFormatPath, (Object)this.dateFormat);
        this.config.set(this.promoteCommandPath, (Object)this.promoteCommand);
        this.config.set(this.demoteCommandPath, (Object)this.demoteCommand);
        try {
            this.config.save(this.configFile);
            System.out.println(String.valueOf(this.consoleLogo) + "... Finished config creation!");
        }
        catch (IOException e) {
            System.err.println(String.valueOf(this.consoleLogo) + "Can't create config file, see info below:");
            e.printStackTrace();
        }
    }
}

