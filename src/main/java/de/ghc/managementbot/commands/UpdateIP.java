package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateIP extends AddIP implements Command {
    private final IPEntry newEntry;
    private final IPEntry oldEntry;

    public UpdateIP(IPEntry newEntry, IPEntry oldEntry) {
        this.newEntry = newEntry;
        this.oldEntry = oldEntry;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentDisplay();
        new Thread(new DeleteMessageThread(60, event.getMessage())).start();
        if (Content.isYes(msg)) {
            if (Content.isStaff(event.getMember())) {
                newEntry.setUpdate(true);
                addIPtoDB(newEntry);
            } else {
                if (oldEntry != null)
                    event.getGuild().getTextChannelById(Data.Channel.zahlenschlacht).sendMessageFormat(
                            "IP-Upate:\nIP: %s -> %s\nName: %s -> %s\nMiner: %d -> %d\nReputation: %d -> %d\nGilde: %s -> %s\nBeschreibung: %s -> %s\nDaten von: %s ",
                            oldEntry.getIP(), newEntry.getIP(), oldEntry.getName(), newEntry.getName(), oldEntry.getMiners(), newEntry.getMiners(), oldEntry.getReputation(), newEntry.getReputation(), oldEntry.getGuildTag(), newEntry.getGuildTag(), oldEntry.getDescription(), newEntry.getDescription(), newEntry.getAddedBy().getName()
                    ).queue(m -> {
                        messageUpdateIp.put(m.getIdLong(), this);
                        m.addReaction("\u2257").queue();
                    });
                else
                    event.getGuild().getTextChannelById(Data.Channel.zahlenschlacht).sendMessage(newEntry.toString()).queue();
            }
        }
        deleteUserUpdateIP(event.getAuthor(), this);
    }

    public boolean updateIP(long msgId) {
        newEntry.setUpdate(true);
        JSONObject response = addIPtoDB(newEntry);
        if (!response.getBoolean("error")) {
            getMessageUpdateIp().remove(msgId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> getCallers() {
        return Collections.emptyList();
    }

    @Override
    public boolean isCalled(String msg) {
        return false;
    }

    @Override
    public boolean isCalled(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public Command createCommand(MessageReceivedEvent event) {
        throw new IllegalStateException("Can't create UpdateIP! Use AddIP!");
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

    private static Map<Long, UpdateIP> messageUpdateIp = new HashMap<>();

    public static Map<Long, UpdateIP> getMessageUpdateIp() {
        return messageUpdateIp;
    }
}
