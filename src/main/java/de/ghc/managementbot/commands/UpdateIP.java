package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

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
        deleteUserUpdateIP(event.getAuthor(), this);
    }
    private static Map<User, UpdateIP> userUpdateIP = new HashMap<>();

    public static Map<User, UpdateIP> getUpdateIP() {
        return userUpdateIP;
    }

    public static void addUserUpdateIP(User user, UpdateIP updateIP) {
        userUpdateIP.put(user, updateIP);
    }
    public static void deleteUserUpdateIP(User user, UpdateIP update) {
        userUpdateIP.remove(user, update);
    }
}
