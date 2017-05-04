package de.ghc.managementbot.content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Date;

public class ServerStatsThread extends Database implements Runnable {

    private int timeout;
    private TextChannel ips;

    public ServerStatsThread(Guild ghc, int timeout) {
        this.timeout = timeout;
        ips = ghc.getTextChannelById("269153131957321728");
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                this.wait(timeout); //startup
                while (true) {
                    String string = getStats();
                    if (string != null) {
                        String[] values = string.split(" ");
                        System.out.println(values[2]);
                        StringBuilder dates = new StringBuilder(), numbers = new StringBuilder();
                        for (int i = 2; i < values.length; i += 2) {
                            dates.append(values[i]).append("\n");
                        }
                        for (int i = 3; i < values.length; i+=2) {
                            numbers.append(values[i]);
                        }
                        ips.sendMessage(new EmbedBuilder().setTitle("IP-Updates", url).setDescription(url).setColor(Content.getRandomColor()).addField("Datum", dates.toString(), true).addField("Updated", numbers.toString(), true).setFooter("Stand: " + new Date(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200").build()).queue();
                        this.wait(timeout);
                    } else
                        this.wait(5000);
                }
            }
        } catch (InterruptedException ignore) {}
    }
}
