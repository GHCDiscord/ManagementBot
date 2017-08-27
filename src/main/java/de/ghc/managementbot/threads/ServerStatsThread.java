package de.ghc.managementbot.threads;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.content.Database;
import net.dv8tion.jda.core.EmbedBuilder;
import org.joda.time.DateTime;

import static de.ghc.managementbot.content.Content.formatDate;

public class ServerStatsThread extends Database implements Runnable {

    private int hour;
    private static final int timeout = 86400000;

    public ServerStatsThread(int hour) {
        this.hour = hour;
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                this.wait((hour * 60 * 60 * 1000) - DateTime.now().getMillisOfDay()); //startup
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
                        Content.getGhc().getTextChannelById(Data.Channel.hackersip).sendMessage(new EmbedBuilder().setTitle("IP-Updates", url).setDescription(url).setColor(Content.getRandomColor()).addField("Datum", dates.toString(), true).addField("Updated", numbers.toString(), true).setFooter("Stand: " + formatDate(), Content.GHCImageURL).build()).queue();
                        this.wait(timeout);
                    } else
                        this.wait(5000);
                }
            }
        } catch (InterruptedException ignore) {}
    }
}
