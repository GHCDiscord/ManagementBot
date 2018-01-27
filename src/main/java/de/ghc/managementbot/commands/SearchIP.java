package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Database;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.ErrorEntry;
import de.ghc.managementbot.entity.IPEntry;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SearchIP extends Database implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] data = event.getMessage().getContentDisplay().split(" ");
        if (data.length < 2)
            event.getChannel().sendMessage("Bitte gebe einen Suchbegriff an").queue();
        else {
            IPEntry ip = getIP(data[1]);
            if (ip == null)
                event.getChannel().sendMessage("Es ist ein unerwarteter Fehler aufgetreten. Bitte versuche es später erneut").queue();
            else if (ip instanceof ErrorEntry)
                event.getChannel().sendMessage(((ErrorEntry) ip).getError()).queue();
            else
                event.getChannel().sendMessage(ip.toString()).queue();
        }
    }

    @Override
    public List<String> getCallers() {
        return Arrays.asList("!search", "!find", "!findip", "!searchip");
    }

    @Override
    public boolean isCalled(String msg) {
        return isCalledFirstWord(msg);
    }
}
