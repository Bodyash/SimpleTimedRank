
package me.bodyash.SimpleTimedRank.versionchecker;

import me.bodyash.SimpleTimedRank.Main.Main;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateChecker {
    private Main main;
    private URL filesFeed;
    private String version;
    private String link;
    private boolean upToDate;

    public UpdateChecker(Main main, String url) {
        block2 : {
            this.main = main;
            try {
                this.filesFeed = new URL(url);
            }
            catch (MalformedURLException e) {
                if (!main.debug) break block2;
                e.printStackTrace();
            }
        }
    }

    public boolean updateNeeded() {
        block4 : {
            try {
                InputStream inputStream = this.filesFeed.openConnection().getInputStream();
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
                Node latestFile = document.getElementsByTagName("item").item(0);
                NodeList children = latestFile.getChildNodes();
                this.version = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
                this.version = this.version.replace("[", "");
                this.version = this.version.replace("]", "");
                this.link = children.item(3).getTextContent();
                if (this.main.debug) {
                    System.out.println(String.valueOf(this.version) + " " + this.link);
                }
                if (!this.main.getDescription().getVersion().equals(this.version)) {
                    this.upToDate = false;
                    return true;
                }
            }
            catch (Exception e) {
                if (!this.main.debug) break block4;
                e.printStackTrace();
            }
        }
        this.upToDate = true;
        return false;
    }

    public void sendConsoleMessages(ConsoleCommandSender console) {
        if (this.upToDate) {
            console.sendMessage(String.valueOf(this.main.getLogo()) + (Object)ChatColor.RED + "Your version of " + this.main.getDescription().getName() + " is up to date!");
        } else {
            console.sendMessage(String.valueOf(this.main.getLogo()) + (Object)ChatColor.RED + "A new version of " + this.main.getDescription().getName() + " is available!");
            console.sendMessage(String.valueOf(this.main.getLogo()) + (Object)ChatColor.RED + "Download it from: " + this.main.getDescription().getWebsite());
        }
    }

    public String getVersion() {
        return this.version;
    }

    public String getLink() {
        return this.link;
    }
}

