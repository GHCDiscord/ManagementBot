package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.content.Database;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static de.ghc.managementbot.content.Content.isVerified;

public class RefreshUser extends Database implements Command {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getType() != ChannelType.PRIVATE)
            new Thread(new DeleteMessageThread(3, event.getMessage())).start();
        Member member = event.getMember();
        if (member == null)
            member = Content.getGHCMember(event.getAuthor());
        if ((event.getGuild() == null && isVerified(member)) || (event.getGuild() != null && event.getTextChannel().equals(event.getGuild().getTextChannelById(Data.Channel.hackersip)) && isVerified(member))) {
            String result = refreshUser(event.getAuthor());
            if (result.equals("success"))
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Account wurde erfolgreich reaktiviert! Viel Spaß mit der IP-Datenbank der GCH unter " + url).queue());
            else if (result.equals("user not found"))
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es wurde kein Account gefunden, der mit deinem Discord-Account verbunden ist. Bitte registiere dich zuerst mit `!register` und deinem gew\u00FCnschten Namen in der Datenbank!").queue());
            else
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es ist ein unerwarteter Fehler aufgetreten! Bitte sende die folgenden Informationen an die Programmierer via @Coding:\n" + result).queue());
        }
    }
}
