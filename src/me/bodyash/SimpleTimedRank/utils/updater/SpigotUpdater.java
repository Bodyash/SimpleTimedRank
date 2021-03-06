package me.bodyash.SimpleTimedRank.utils.updater;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.bodyash.SimpleTimedRank.Main.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpigotUpdater {
	private String WRITE_STRING;
	private String version;
	private String oldVersion;
	private SpigotUpdater.UpdateResult result = SpigotUpdater.UpdateResult.DISABLED;
	private HttpURLConnection connection;
	private boolean updateFound;

	public enum UpdateResult {
		NO_UPDATE,
		DISABLED,
		FAIL_SPIGOT,
		SPIGOT_UPDATE_AVAILABLE,
		MAJOR_SPIGOT_UPDATE_AVAILABLE
	}

	public SpigotUpdater(JavaPlugin plugin) {
		String RESOURCE_ID = "26566";
		oldVersion = plugin.getDescription().getVersion().replaceAll("-SNAPSHOT-", ".");
		try {
			String QUERY = "/api/general.php";
			String HOST = "http://www.spigotmc.org";
			connection = (HttpURLConnection) new URL(HOST + QUERY).openConnection();
		} catch (IOException e) {
			result = UpdateResult.FAIL_SPIGOT;
			return;
		}

		String API_KEY = "98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4";
		WRITE_STRING = "key=" + API_KEY + "&resource=" + RESOURCE_ID;
		runSpigot();
	}

	private void runSpigot() {
		connection.setDoOutput(true);
		try {
			String REQUEST_METHOD = "POST";
			connection.setRequestMethod(REQUEST_METHOD);
			connection.getOutputStream().write(WRITE_STRING.getBytes("UTF-8"));
		} catch (IOException e) {
			result = UpdateResult.FAIL_SPIGOT;
		}
		String version;
		try {
			version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
		} catch (Exception e) {
			result = UpdateResult.FAIL_SPIGOT;
			return;
		}
		if (version.length() <= 7) {
			this.version = version.replace("[^A-Za-z]", "").replace("|", "");
			spigotCheckUpdate();
			return;
		}
		result = UpdateResult.FAIL_SPIGOT;
	}

	private void spigotCheckUpdate() {
		Integer oldVersion = Integer.parseInt(this.oldVersion.replace(".", ""));
		Integer currentVersion = Integer.parseInt(this.version.replace(".", ""));
		if (oldVersion < currentVersion) {
			String[] localParts = this.oldVersion.split("\\.");
			String[] remoteParts = this.version.split("\\.");
			if (Integer.parseInt(localParts[0]) < Integer.parseInt(remoteParts[0])) {
				result = UpdateResult.MAJOR_SPIGOT_UPDATE_AVAILABLE;
				this.updateFound = true;
			} else {
				result = UpdateResult.SPIGOT_UPDATE_AVAILABLE;
				this.updateFound = true;
			}
		} else {
			result = UpdateResult.NO_UPDATE;
			this.updateFound = false;
		}
	}
	

	public UpdateResult getResult() {
		return result;
	}

	public String getVersion() {
		return version;
	}
	
    public void sendConsoleMessages(ConsoleCommandSender console, Main main) {
        if (!this.updateFound) {
            console.sendMessage(String.valueOf(main.getLogo()) + (Object)ChatColor.GREEN + "Your version of " + main.getDescription().getName() + " is up to date!");
        } else {
            console.sendMessage(String.valueOf(main.getLogo()) + (Object)ChatColor.RED + "A new version of " + main.getDescription().getName() + " is available!");
            console.sendMessage(String.valueOf(main.getLogo()) + (Object)ChatColor.RED + "Download it from: " + main.getDescription().getWebsite() + "/updates");
        }
    }
    
    public boolean isUpdateFound(){
    	return updateFound;
    }

}