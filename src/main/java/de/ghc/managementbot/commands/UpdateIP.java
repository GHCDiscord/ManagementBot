package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UpdateIP implements Command {
    private IPEntry entry;

    public UpdateIP(IPEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent();
        new Thread(new DeleteMessageThread(60, event.getMessage())).start();
        if (msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y")) {
            Content.getGhc().getTextChannelById(Data.Channel.zahlenschlacht).sendMessage(entry.toString() + "Daten von: " + Content.getGHCMember(entry.getAddedBy()).getEffectiveName()).queue();
        }
        Content.deleteUserUpdateIP(event.getAuthor(), this);
    }
}
