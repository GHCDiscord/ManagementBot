package de.ghc.managementbot.entity;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public interface Command {
    void onMessageReceived(MessageReceivedEvent event);
    List<String> getCallers();
    default boolean isCalled(MessageReceivedEvent event) {
        return isCalled(event.getMessage().getContentDisplay());
    }
    default boolean isCalled(String msg) {
        List<String> callers = getCallers();
        return callers.contains(msg.toLowerCase());
    }
    default Command createCommand(MessageReceivedEvent event) {
        return this;
    }
    default boolean isCalledFirstWord(String msg) {
        List<String> callers = getCallers();
        return callers.contains(msg.toLowerCase().split(" ")[0]);
    }
}
