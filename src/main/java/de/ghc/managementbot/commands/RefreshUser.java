package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.content.Database;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

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
            final JSONObject result = refreshUser(event.getAuthor());
            if (!result.getBoolean("error"))
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Account wurde erfolgreich reaktiviert! Viel Spaß mit der IP-Datenbank der GCH unter " + url).queue());
            else if (result.has("msgDiscord") && result.getString("msgDiscord").equals("Discord User nicht gefunden!"))
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es wurde kein Account gefunden, der mit deinem Discord-Account verbunden ist. Bitte registiere dich zuerst mit `!register` und deinem gew\u00FCnschten Namen in der Datenbank!").queue());
            else
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es ist ein unerwarteter Fehler aufgetreten: " + getError(result)).queue());

        }
    }

    private String getError(JSONObject response) {
        String error = "";
        if (response.has("msgDiscord"))
            error += response.getString("msgDiscord");
        if (response.has("msgToken"))
            error += error.isEmpty() ? response.getString("msgToken") : ", " + response.getString("msgToken");
        return error;
    }

    @Override
    public List<String> getCallers() {
        return Collections.singletonList("!refresh");
    }
}
