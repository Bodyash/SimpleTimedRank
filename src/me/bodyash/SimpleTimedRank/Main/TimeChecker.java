
package me.bodyash.SimpleTimedRank.Main;

import me.bodyash.SimpleTimedRank.Main.ConfigUsers;
import me.bodyash.SimpleTimedRank.Main.Main;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TimeChecker implements Listener {
	private Main main;
	private ConfigUsers confU;

	public TimeChecker(Main main, ConfigUsers confU) {
		this.main = main;
		this.confU = confU;
	}

	public Long getPlayerDaysLeft(String playerName) {
		GregorianCalendar until;
		GregorianCalendar now;
		block4: {
			try {
				now = new GregorianCalendar();
				until = this.parseDate(this.confU.getUserData(playerName, "untilDate"),
						this.confU.getUserData(playerName, "UntilTime"));
				if (now != null && until != null)
					break block4;
				return null;
			} catch (Exception e) {
				return null;
			}
		}
		now.add(2, 1);
		now.add(1, 1900);
		if (this.main.debug) {
			System.out.println("Until: Date: " + until.getTime().getDate() + " Month: " + until.getTime().getMonth()
					+ " Year: " + until.getTime().getYear() + " Hours: " + until.getTime().getHours() + " Minutes: "
					+ until.getTime().getMinutes() + " Seconds: " + until.getTime().getSeconds());
			System.out.println("Now: Date: " + now.getTime().getDate() + " Month: " + now.getTime().getMonth()
					+ " Year: " + now.getTime().getYear() + " Hours: " + now.getTime().getHours() + " Minutes: "
					+ now.getTime().getMinutes() + " Seconds: " + now.getTime().getSeconds());
		}
		return TimeUnit.MILLISECONDS.toDays(until.getTimeInMillis())
				- TimeUnit.MILLISECONDS.toDays(now.getTimeInMillis());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerJoin(PlayerJoinEvent e) {
		this.checkPlayer(e.getPlayer());
	}

	public void checkPlayer(Player p) {
		try {
			if (this.confU.getUserData(p.getName(), "promotedRank") != null
					&& this.confU.getUserData(p.getName(), "status").compareToIgnoreCase("-1") != 0) {
				GregorianCalendar until = this.parseDate(this.confU.getUserData(p.getName(), "untilDate"),
						this.confU.getUserData(p.getName(), "untilTime"));
				GregorianCalendar now = new GregorianCalendar();
				GregorianCalendar newNow = this.parseDate(this.parseDateToString(now.getTime()),
						this.parseTimeToString(now.getTime()));
				if (this.main.debug) {
					System.out.println(
							"Stunden: " + until.getTime().getHours() + " Minuten: " + until.getTime().getMinutes());
					System.out.println(
							"Stunden: " + newNow.getTime().getHours() + " Minuten: " + newNow.getTime().getMinutes());
				}
				if (until != null && newNow != null) {
					if (newNow.getTimeInMillis() - until.getTimeInMillis() > 0) {
						System.out.println(
								String.valueOf(until.getTimeInMillis() - newNow.getTimeInMillis()) + " Zeit 1");
						if (this.confU.getUserData(p.getName(), "status").compareToIgnoreCase("-1") != 0) {
							p.sendMessage(String.valueOf(this.main.getLogo()) + this.main.getTimeExpiredMsg());
							Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
									(String) this.main.parseSyntax(this.main.getDemoteCommand(), p.getName(),
											this.confU.getUserData(p.getName(), "promotedRank"),
											this.confU.getUserData(p.getName(), "oldRank")));
							System.out.println(String.valueOf(this.main.getConsoleLogo()) + "The player " + p.getName()
									+ " was demoted to " + this.confU.getUserData(p.getName(), "oldRank") + ".");
							this.confU.setUserTimeExpired(p.getName());
							return;
						}
					}
					if (newNow.getTime().getDay() - until.getTime().getDay() == 0) {
						System.out.println(
								String.valueOf(until.getTimeInMillis() - newNow.getTimeInMillis()) + " Zeit 2");
						p.sendMessage(String.valueOf(this.main.getLogo()) + this.main.parseSyntax(
								this.main.getLastDayMsg(), p.getName(), this.confU.getUserData(p.getName(), "oldRank"),
								this.confU.getUserData(p.getName(), "promotedRank")));
						return;
					}
				}
			}
		} catch (Exception e2) {
			System.err.println(String.valueOf(this.main.getConsoleLogo())
					+ "Error: An error occurred on the method \"onPlayerJoin\".");
			System.err.println(String.valueOf(this.main.getConsoleLogo())
					+ "Error: Be sure that all the dates and times are in the right format!");
			System.err.println(String.valueOf(this.main.getConsoleLogo()) + "Please see infos below:");
			e2.printStackTrace();
		}
	}

	public String parseDateToString(Date date) {
		String result = null;
		try {
			SimpleDateFormat until = new SimpleDateFormat(this.main.getDateFormat());
			result = until.format(date);
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		return result;
	}
	
	public String parseDateToString(GregorianCalendar date) {
		String result = null;
		try {
			SimpleDateFormat until = new SimpleDateFormat(this.main.getDateFormat());
			result = until.format(date);
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		return result;
	}

	public String parseTimeToString(Date date) {
		String result = null;
		try {
			SimpleDateFormat until = new SimpleDateFormat("HH:mm");
			result = until.format(date);
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		return result;
	}

	public GregorianCalendar parseDate(String stringDate, String stringTime) {
		GregorianCalendar greg = null;
		try {
			StringTokenizer date = new StringTokenizer(stringDate, ".");
			int day = Integer.parseInt(date.nextToken());
			int month = Integer.parseInt(date.nextToken());
			int year = Integer.parseInt(date.nextToken());
			StringTokenizer time = new StringTokenizer(stringTime, ":");
			int hour = Integer.parseInt(time.nextToken());
			int minute = Integer.parseInt(time.nextToken());
			greg = new GregorianCalendar(year + 1900, month, day, hour, minute);
			if (this.main.debug) {
				System.out.println(String.valueOf(greg.getTime().getDate()) + "." + greg.getTime().getMonth() + "."
						+ greg.getTime().getYear());
			}
		} catch (Exception e) {
			return null;
		}
		return greg;
	}

	public GregorianCalendar parseNumsAndLetters(String stringDate, GregorianCalendar currentTime) {
		GregorianCalendar greg = new GregorianCalendar();
		greg.setTime(currentTime.getTime());
		stringDate.toLowerCase();
		int days = 0;
		if (stringDate.endsWith("m")) {
			if (stringDate.length() >= 3) {
				days = Integer.parseInt(stringDate.substring(0, 2))*30;
			} else if (stringDate.length() == 2) {
				days = Integer.parseInt(stringDate.substring(0, 1))*30;
			}
		}
		
		if (stringDate.endsWith("d")){
			days = Integer.parseInt(stringDate.substring(0, stringDate.length() - 1));
		}
		
		greg.add(Calendar.DAY_OF_YEAR, days);

		return greg;
	}
}
