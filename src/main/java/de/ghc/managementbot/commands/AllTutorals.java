package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AllTutorals implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        if ((event.getMember() != null && Content.isModerator(event.getMember())) || event.getMember() == null) {
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("Tutorials")
                    .addField("FAQ", "[hier](https://docs.google.com/document/d/18h_Ik023Ax9eGUxSCzVszhTask1y5ayP2TweVFNMdHE/pub)", true)
                    .addField("Gilden", "[hier](http://forum.hackerz.online/viewtopic.php?f=12&t=78)", true)
                    .addField("Taktiken", "[hier](https://forum.hackerz.online/viewtopic.php?f=10&t=334)", true)
                    .setColor(Content.getRandomColor())
                    .setFooter("", Content.GHCImageURL)
                    .build()).queue();
        }
    }
}
