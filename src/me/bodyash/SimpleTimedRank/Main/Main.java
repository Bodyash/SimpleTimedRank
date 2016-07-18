/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package me.bodyash.SimpleTimedRank.Main;

import me.bodyash.SimpleTimedRank.Main.ConfigUsers;
import me.bodyash.SimpleTimedRank.Main.ConfigUtility;
import me.bodyash.SimpleTimedRank.Main.TimeChecker;
import me.bodyash.SimpleTimedRank.versionchecker.UpdateChecker;
import java.io.PrintStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {
	PluginDescriptionFile descFile;
	private String logo;
	private String left;
	private String right;
	private String noPermissions;
	private String consoleLogo;
	private ConfigUsers confUsers;
	private ConfigUtility confUtility;
	private TimeChecker timeChecker;
	private UpdateChecker updateChecker;
	private ConsoleCommandSender console;
	public boolean debug = false;

	public void onEnable() {
		this.setUp();
		this.confUsers = new ConfigUsers(this);
		this.confUtility = new ConfigUtility(this);
		this.timeChecker = new TimeChecker(this, this.confUsers);
		this.getServer().getPluginManager().registerEvents((Listener) this.timeChecker, (Plugin) this);
		this.noPermissions = this.getNoPermMessage();
		if (this.isCheckVersion()) {
			this.updateChecker = new UpdateChecker(this,
					"http://dev.bukkit.org/bukkit-plugins/simplepromote/files.rss");
			this.updateChecker.updateNeeded();
			this.updateChecker.sendConsoleMessages(this.console);
		} else {
			this.updateChecker = null;
			this.console.sendMessage(String.valueOf(this.consoleLogo) + "You disabled version checking! :(");
			this.console.sendMessage(String.valueOf(this.consoleLogo)
					+ "If you want to be informed about a new version of " + this.descFile.getName());
			this.console.sendMessage(String.valueOf(this.consoleLogo) + "Enable it in the config. :)");
		}
		this.console.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.GREEN + "Successfully enabled");
		System.out.println("[" + this.descFile.getName() + "] Version " + this.descFile.getVersion() + " by "
				+ this.descFile.getAuthors() + ".");
		if (this.getCheckMethod().compareToIgnoreCase("interval") == 0
				|| this.getCheckMethod().compareToIgnoreCase("all") == 0) {
			long interval = this.getInterval() * 1200;
			this.getServer().getScheduler().scheduleAsyncRepeatingTask((Plugin) this, new Runnable() {

				@Override
				public void run() {
					Player[] arrplayer = (Player[]) Bukkit.getOnlinePlayers().toArray();
					int n = arrplayer.length;
					int n2 = 0;
					while (n2 < n) {
						Player p = arrplayer[n2];
						Main.this.timeChecker.checkPlayer(p);
						++n2;
					}
				}
			}, 0, interval);
		}
	}

	public void onDisable() {
		this.console.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.GREEN + "Successfully disabled");
	}

	private void setUp() {
		this.descFile = this.getDescription();
		this.logo = (Object) ChatColor.GREEN + "[" + (Object) ChatColor.YELLOW + this.descFile.getName()
				+ (Object) ChatColor.GREEN + "]: " + (Object) ChatColor.GRAY;
		this.left = (Object) ChatColor.GREEN + "[" + (Object) ChatColor.WHITE;
		this.right = (Object) ChatColor.GREEN + "]" + (Object) ChatColor.GRAY;
		this.consoleLogo = "[" + this.descFile.getName() + "] ";
		this.console = this.getServer().getConsoleSender();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("sp")) {
			this.sp(sender, command, label, args);
			return true;
		}
		if (label.equalsIgnoreCase("tempRank") || label.equalsIgnoreCase("upRank")) {
			this.tempRank(sender, command, label, args);
			return true;
		}
		if (label.equalsIgnoreCase("downRank")) {
			this.downRank(sender, command, label, args);
			return true;
		}
		if (label.equalsIgnoreCase("timeLeft") || label.equalsIgnoreCase("daysLeft")
				|| label.equalsIgnoreCase("dayLeft")) {
			this.timeLeft(sender, command, label, args);
			return true;
		}
		return false;
	}

	private void listAllCommands(CommandSender sender) {
		int i = 1;
		sender.sendMessage(
				String.valueOf(this.logo) + (Object) ChatColor.DARK_GREEN + "--------- Command List ---------");
		if (sender.hasPermission("simpletimepromotion.help")) {
			sender.sendMessage(String.valueOf(this.left) + i + this.right + (Object) this.changeColor(i)
					+ " /sp help: Prints you the commands of this plugin");
			++i;
		}
		if (sender.hasPermission("simpletimepromotion.reload")) {
			sender.sendMessage(String.valueOf(this.left) + i + this.right + (Object) this.changeColor(i)
					+ " /sp reload: Reloads the whole plugin.");
			++i;
		}
		if (sender.hasPermission("simpletimepromotion.tempRank")) {
			sender.sendMessage(String.valueOf(this.left) + i + this.right + (Object) this.changeColor(i)
					+ " /tempRank [Player] [NewRank] [DateUntil] (time) [OldRank]: Promotes a player to [NewRank] until [DateUntil] (time).");
			++i;
		}
		if (sender.hasPermission("simpletimepromotion.downRank")) {
			sender.sendMessage(String.valueOf(this.left) + i + this.right + (Object) this.changeColor(i)
					+ " /downRank [Player]: Demotes a Player to his old rank.");
			++i;
		}
		if (sender.hasPermission("simpletimepromotion.timeLeft")) {
			sender.sendMessage(String.valueOf(this.left) + i + this.right + (Object) this.changeColor(i)
					+ " /daysLeft: Shows the remaining time until your rank will expire.");
			++i;
		}
	}

	private ChatColor changeColor(int i) {
		ChatColor c1 = ChatColor.GOLD;
		ChatColor c2 = ChatColor.YELLOW;
		int id = i % 2;
		switch (id) {
		case 1: {
			return c1;
		}
		}
		return c2;
	}

	private boolean sp(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("simpletimepromotion.help")) {
				sender.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.GOLD
						+ "If you need help with the commands type '/sp help'.");
				return false;
			}
			sender.sendMessage(this.noPermissions);
			return false;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help")) {
				if (sender.hasPermission("simpletimepromotion.help")) {
					this.listAllCommands(sender);
					return true;
				}
				sender.sendMessage(String.valueOf(this.logo) + this.noPermissions);
				return false;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("simpletimepromotion.reload")) {
					this.onReload(sender);
					sender.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.DARK_GREEN + "Plugin Reloaded !");
					return true;
				}
				sender.sendMessage(String.valueOf(this.logo) + this.noPermissions);
				return false;
			}
		}
		if (sender.hasPermission("simpletimepromotion.help")) {
			sender.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.GOLD
					+ "If you need help with the commands type '/sp help'.");
			return false;
		}
		sender.sendMessage(this.noPermissions);
		return false;
	}

	public boolean onReload(CommandSender sender) {
		this.getServer().getPluginManager().disablePlugin((Plugin) this);
		this.getServer().getPluginManager().enablePlugin((Plugin) this);
		return true;
	}

	private boolean tempRank(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 4 || args.length > 5) {
			if (sender.hasPermission("simpletimepromotion.help")) {
				sender.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.GOLD
						+ "If you need help with the commands type '/sp help'.");
				sender.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.GOLD
						+ "Correct usage: /tempRank [Player] [NewRank] [DateUntil] (time) [OldRank]");
				return false;
			}
			sender.sendMessage(this.noPermissions);
			return false;
		}
		if (sender.hasPermission("simpletimepromotion.tempRank")) {
			GregorianCalendar timeNow = new GregorianCalendar();
			if (args.length == 4) {
				if (this.checkIfCorrectDate(args[2])) {
					try {
						this.confUsers.addUser(args[0], args[1],
								this.timeChecker
										.parseDateToString(this.timeChecker.parseNumsAndLetters(args[2], timeNow).getTime()),
								"00:00", this.timeChecker.parseDateToString(timeNow.getTime()),
								this.timeChecker.parseTimeToString(timeNow.getTime()), args[3]);
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								(String) this.parseSyntax(this.getPromoteCommand(), args[0], args[1], args[3]));
						sender.sendMessage(String.valueOf(this.logo) + "The player " + args[0]
								+ " has been promoted to the rank " + args[1] + " until " + args[2] + "!");
						System.out.println(String.valueOf(this.consoleLogo) + "The player " + args[0]
								+ " has been promoted from the player " + sender.getName() + " from " + args[3] + " to "
								+ args[1] + " until " + args[2] + "!");
						return true;
					} catch (Exception e) {
						sender.sendMessage(String.valueOf(this.logo)
								+ "Error: An error occurred while trying to promote the player " + args[0] + "!");
						sender.sendMessage(String.valueOf(this.logo) + "Error: The player was not promoted.");
						System.err.println(String.valueOf(this.consoleLogo)
								+ "Error: An error occurred while trying to promote the player " + args[0] + "!");
						System.err.println(String.valueOf(this.consoleLogo) + "Error: The player was not promoted.");
						return false;
					}
				}
				sender.sendMessage(String.valueOf(this.logo) + "Correct date format: day.month.year");
				sender.sendMessage(String.valueOf(this.logo) + "Example: /temprank Player Vip 20.04.2013 Builder");
			}
			if (args.length == 5) {
				if (this.checkIfCorrectDate(args[2]) && this.checkIfCorrectTime(args[3])) {
					try {
						this.confUsers.addUser(args[0], args[1], this.timeChecker
								.parseDateToString(this.timeChecker.parseNumsAndLetters(args[2], timeNow).getTime()), args[3],
								this.timeChecker.parseDateToString(timeNow.getTime()),
								this.timeChecker.parseTimeToString(timeNow.getTime()), args[4]);
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								(String) this.parseSyntax(this.getPromoteCommand(), args[0], args[1], args[3]));
						sender.sendMessage(String.valueOf(this.logo) + "The player " + args[0]
								+ " has been promoted to the rank " + args[1] + " until " + args[2] + "!");
						System.out.println(String.valueOf(this.consoleLogo) + "The player " + args[0]
								+ " has been promoted from the player " + sender.getName() + " from " + args[4] + " to "
								+ args[1] + " until " + args[2] + "!");
						return true;
					} catch (Exception e) {
						sender.sendMessage(String.valueOf(this.logo)
								+ "Error: An error occurred while trying to promote the player " + args[0] + "!");
						sender.sendMessage(String.valueOf(this.logo) + "Error: The player was not promoted.");
						System.err.println(String.valueOf(this.consoleLogo)
								+ "Error: An error occurred while trying to promote the player " + args[0] + "!");
						System.err.println(String.valueOf(this.consoleLogo) + "Error: The player was not promoted.");
						return false;
					}
				}
				if (!this.checkIfCorrectDate(args[2])) {
					sender.sendMessage(String.valueOf(this.logo) + "Correct date format: " + this.getDateFormat());
				}
				if (!this.checkIfCorrectTime(args[3])) {
					sender.sendMessage(String.valueOf(this.logo) + "Correct time format: hours:minutes");
				}
				sender.sendMessage(
						String.valueOf(this.logo) + "Example: /temprank Player Vip 20.04.2013 20:12 Builder");
			}
		} else {
			sender.sendMessage(this.noPermissions);
			return false;
		}
		return false;
	}

	/*
	 * Enabled force condition propagation Lifted jumps to return sites
	 */
	private boolean downRank(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("simpletimepromotion.downRank")) {
			if (args.length < 1 || args.length > 1) {
				if (sender.hasPermission("simpletimepromotion.help")) {
					sender.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.GOLD
							+ "If you need help with the commands type '/sp help'.");
					return false;
				}
				sender.sendMessage(this.noPermissions);
				return false;
			}
			if (args.length == 1) {
				try {
					if (this.confUsers.getUserData(args[0], "oldRank") != null
							&& this.confUsers.getUserData(args[0], "status").compareToIgnoreCase("-1") != 0) {
						this.confUsers.setUserTimeExpired(args[0]);
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								(String) this.parseSyntax(this.getDemoteCommand(), args[0],
										this.confUsers.getUserData(args[0], "newRank"),
										this.confUsers.getUserData(args[0], "oldRank")));
						return true;
					}
					sender.sendMessage(String.valueOf(this.logo) + "Can't demote the player " + args[0]
							+ " because the player isn't promoted and/or isn't registered!");
					return false;
				} catch (Exception e) {
					try {
						sender.sendMessage(String.valueOf(this.logo) + "Can't demote the player " + args[0]
								+ " because the player isn't promoted and/or isn't registered!");
						return false;
					} catch (Exception e1) {
						System.err.println(String.valueOf(this.consoleLogo)
								+ "An error occurred while performing the command downRank!");
						return false;
					}
				}
			}
		}
		sender.sendMessage(this.noPermissions);
		return false;
	}

	private boolean timeLeft(CommandSender sender, Command command, String label, String[] args) {
		block16: {
			block17: {
				block18: {
					block12: {
						block13: {
							block15: {
								block14: {
									block10: {
										block11: {
											try {
												if (args.length <= 1)
													break block10;
												if (!sender.hasPermission("simpletimepromotion.help"))
													break block11;
												sender.sendMessage(String.valueOf(this.logo) + (Object) ChatColor.GOLD
														+ "If you need help with the commands type '/sp help'.");
												return false;
											} catch (Exception e) {
												sender.sendMessage(
														String.valueOf(this.logo) + "Error: Can't check the time!");
												return false;
											}
										}
										sender.sendMessage(this.noPermissions);
										return false;
									}
									if (args.length != 0)
										break block12;
									if (!sender.hasPermission("simpletimepromotion.timeLeft"))
										break block13;
									Long days = this.timeChecker.getPlayerDaysLeft(sender.getName());
									if (days == null)
										break block14;
									sender.sendMessage(String.valueOf(this.logo) + "Time left as "
											+ this.confUsers.getUserData(sender.getName(), "promotedRank") + ": " + days
											+ " Day(s)");
									return true;
								}
								if (this.confUsers.getUserData(sender.getName(), "status")
										.compareToIgnoreCase("-1") != 0)
									break block15;
								sender.sendMessage(String.valueOf(this.logo) + this.getTimeExpiredMsg());
								return false;
							}
							sender.sendMessage(String.valueOf(this.logo) + this.getCantCheckTimeMsg());
							return false;
						}
						sender.sendMessage(this.noPermissions);
						return false;
					}
					if (args.length != 1)
						break block16;
					if (!sender.hasPermission("simpletimepromotion.timeLeft.others"))
						break block17;
					Long days = this.timeChecker.getPlayerDaysLeft(args[0]);
					if (days == null)
						break block18;
					sender.sendMessage(String.valueOf(this.logo) + "Time left " + days + " Days");
					return true;
				}
				sender.sendMessage(String.valueOf(this.logo) + "Can't find the player " + args[0] + ".");
				return false;
			}
			sender.sendMessage(this.noPermissions);
			return false;
		}
		return false;
	}

	private boolean checkIfCorrectDate(String stringDateToCheck) {
		int temp = 0;
		if (stringDateToCheck.endsWith("d") || stringDateToCheck.endsWith("m")) {
			if (stringDateToCheck.length() > 0 && stringDateToCheck.length() < 3) {
				try {

					if (stringDateToCheck.endsWith("m")) {
						if (stringDateToCheck.length() >= 3) {
							temp = Integer.parseInt(stringDateToCheck.substring(0, 2)) * 30;
						} else if (stringDateToCheck.length() == 2) {
							temp = Integer.parseInt(stringDateToCheck.substring(0, 1)) * 30;
						}
					}

					if (stringDateToCheck.endsWith("d")) {
						temp = Integer.parseInt(stringDateToCheck.substring(0, stringDateToCheck.length() - 1));
					}

				} catch (Exception e) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkIfCorrectTime(String stringTimeToCheck) {
		StringTokenizer args;
		block4: {
			try {
				args = new StringTokenizer(stringTimeToCheck, ":");
				int hours = Integer.parseInt(args.nextToken());
				if (hours >= 0 && hours <= 23)
					break block4;
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		int minutes = Integer.parseInt(args.nextToken());
		if (minutes < 0 || minutes > 59) {
			return false;
		}
		return true;
	}

	public String parseSyntax(String msg, String player, String newRank, String oldRank) {
		if (player != null) {
			msg = msg.replace("%player%", player);
		}
		if (newRank != null) {
			msg = msg.replace("%newRank%", newRank);
		}
		if (oldRank != null) {
			msg = msg.replace("%oldRank%", oldRank);
		}
		return msg;
	}

	public String getNoPermMessage() {
		return ChatColor.translateAlternateColorCodes((char) '&', (String) this.confUtility.noPermMessage);
	}

	public String getConsoleLogo() {
		return this.consoleLogo;
	}

	public String getLogo() {
		return this.logo;
	}

	public String getDateFormat() {
		return this.confUtility.dateFormat;
	}

	public String getPromoteCommand() {
		return this.confUtility.promoteCommand;
	}

	public String getDemoteCommand() {
		return this.confUtility.demoteCommand;
	}

	public String getLastDayMsg() {
		return ChatColor.translateAlternateColorCodes((char) '&', (String) this.confUtility.lastDayMsg);
	}

	public String getTimeExpiredMsg() {
		return ChatColor.translateAlternateColorCodes((char) '&', (String) this.confUtility.timeExpiredMsg);
	}

	public String getCantCheckTimeMsg() {
		return ChatColor.translateAlternateColorCodes((char) '&', (String) this.confUtility.cantCheckTimeMsg);
	}

	public String getCheckMethod() {
		return this.confUtility.checkMethod;
	}

	public long getInterval() {
		return this.confUtility.interval;
	}

	public boolean isCheckVersion() {
		return this.confUtility.checkVersion;
	}

}
