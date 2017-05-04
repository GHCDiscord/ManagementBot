package de.ghc.managementbot.content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Date;

public class ServerStats extends Database implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String string = getStats();
        if (string != null) {
            String[] values = string.split(" ");
            System.out.println(values[2]);
            StringBuilder dates = new StringBuilder(), numbers = new StringBuilder();
            for (int i = 2; i < values.length; i += 2) {
                dates.append(values[i]).append("\n");
            }
            for (int i = 3; i < values.length; i += 2) {
                numbers.append(values[i]);
            }
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("IP-Updates").setDescription(url).setColor(Content.getRandomColor()).addField("Datum", dates.toString(), true).addField("Updated", numbers.toString(), true).setFooter("Stand: " + new Date(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200").build()).queue();
        }
    }
}
