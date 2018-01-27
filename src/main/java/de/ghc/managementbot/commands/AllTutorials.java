package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AllTutorials implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        if (Content.isBotModerator(Content.getGHCMember(event.getAuthor())) || event.getMember() == null) {
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("Tutorials")
                    .addField("FAQ", String.format("[%s], (%s \"%s\")","hier", "https://docs.google.com/document/d/18h_Ik023Ax9eGUxSCzVszhTask1y5ayP2TweVFNMdHE/pub",  "FAQ"), true)
                    .addField("Gilden", String.format("[%s], (%s \"%s\")", "hier", "http://forum.hackerz.online/viewtopic.php?f=12&t=78", "Gilden"), true)
                    .addField("Taktiken", String.format("[%s], (%s \"%s\")", "hier", "https://forum.hackerz.online/viewtopic.php?f=10&t=334", "Taktiken"), true)
                    .setColor(Content.getRandomColor())
                    .setFooter("", Content.GHCImageURL)
                    .build()).queue();
        }
    }

    @Override
    public List<String> getCallers() {
        return Arrays.asList("!alltutorials", "!alltut", "!alltutorial");
    }
}
