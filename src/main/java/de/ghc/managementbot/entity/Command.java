package de.ghc.managementbot.entity;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    void onMessageReceived(MessageReceivedEvent event);
}
