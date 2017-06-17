package de.ghc.managementbot.threads;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MarketAPIThread implements Runnable {

    private String oldVersion;

    public MarketAPIThread() {
        oldVersion = getVersionNumber();
    }

    public static void main(String[] args) {
        System.out.println(getLastUpdateDate());
    }

    public static String getGameInfo() {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet("https://play.google.com/store/apps/details?id=net.okitoo.hackers");

            HttpResponse response = client.execute(get);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder d = new StringBuilder();
            {
                String line;
                while ((line = reader.readLine()) != null)
                    d.append(line);
            }
            return d.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionNumber() {
        return getVersionNumber(getGameInfo());
    }

    public static String getVersionNumber(String data) {
        if (data != null && data.contains("\"softwareVersion\"")) {
            int i = data.indexOf("\"softwareVersion\"");
            return data.substring(i + 19, data.indexOf("</div> </div> <div class=\"meta-info\"> <div class=\"title\">Erforderliche Android-Version</div>"));
        }
        return null;
    }

    public static String getUpdateNotes() {
        return getUpdateNotes(getGameInfo());
    }

    public static String getUpdateNotes(String data) {
        if (data != null && data.contains("<div class=\"recent-change\">")) {
            int i = data.indexOf("<div class=\"recent-change\">");
            String changes = data.substring(i + 27, data.indexOf("</div> <div class=\"show-more-end\" jsaction=\"click:vhaaFf\"></div>"));
            return changes.replace("</div><div class=\"recent-change\">", "\n");
        }
        return null;
    }

    public static String getLastUpdateDate() {
        return getLastUpdateDate(getGameInfo());
    }

    public static String getLastUpdateDate(String data) {
        if (data != null && data.contains("<span class=\"review-date\">")) {
            int i = data.indexOf("<span class=\"review-date\">");
            return data.substring(i + 26, data.indexOf("</span> <a class=\"reviews-permalink\""));
        }
        return null;
    }

    @Override
    public void run() {
        while (true) {
            try {
              synchronized (this) {
                  this.wait(60000);
              }
              String data = getGameInfo();
              String v = getVersionNumber(data);
              if (v != null && !v.equals(oldVersion)) {
                  Content.getGhc().getTextChannelById(Data.general).sendMessage(new EmbedBuilder()
                          .setColor(new Color(59, 176, 65))
                          .setTitle("Neue Version verfügbar!", "https://play.google.com/store/apps/details?id=net.okitoo.hackers")
                          .setAuthor("Hackers - Hacking simulator", "https://play.google.com/store/apps/details?id=net.okitoo.hackers", "https://lh3.googleusercontent.com/iZK3i8S-dUl76VOzwalBSLvOi7z1XfSp5Evjy4vn4XtQ67gf3Y9daGns-2S7-eTsKg=w300-rw")
                          .setDescription(getUpdateNotes(data))
                          .addField("Alte Version", oldVersion, true)
                          .addField("Neue Version", v, true)
                          .setFooter("Update um: " + Content.formatDate(), Content.GHCImageURL)
                          //.setThumbnail()
                          .build()
                  ).queue();
                  oldVersion = v;
              }
            } catch (InterruptedException ignore) {}
        }
    }
}
