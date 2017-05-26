package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.threads.MarketAPIThread;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Version implements Command{
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        event.getChannel().sendTyping().queue();
        String data = MarketAPIThread.getGameInfo();
        event.getChannel().sendMessage(new EmbedBuilder()
                .setColor(Content.getRandomColor())
                .setTitle("Aktuelle Version: " + MarketAPIThread.getVersionNumber(data), "https://play.google.com/store/apps/details?id=net.okitoo.hackers")
                .setDescription("Change Notes:\n" + MarketAPIThread.getUpdateNotes(data))
                //.addField("Letztes Update: ", MarketAPIThread.getLastUpdateDate(data), true)
                //.setFooter("Stand: " + Content.formatDate(), Content.GHCImageURL)
                .setFooter("Letztes Update: " + MarketAPIThread.getLastUpdateDate(data), Content.GHCImageURL)
                .build()
        ).queue();
    }
}
