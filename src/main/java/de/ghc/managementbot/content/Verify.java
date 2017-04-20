package de.ghc.managementbot.Content;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static ManagementBot.Content.Content.addRole;

public class Verify implements Command {

    private static final int deleteTimeVerify = 600;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getGuild() != null) {
            if (!event.getMember().getRoles().contains(event.getGuild().getRolesByName("verified", true).get(0))) {
                addRole(event.getMember(), event.getGuild(), event.getGuild().getRolesByName("verified", true).get(0));
                Message message = new MessageBuilder().append(event.getAuthor()).append(" ist nun verifiziert!").build();
                event.getMessage().getChannel().sendMessage(message).queue( m ->
                        new Thread(new DeleteMessageThread(deleteTimeVerify, m)).start()
                );

            } else {
                Message message = new MessageBuilder().append(event.getAuthor()).append(" ist bereits verifiziert").build();
                event.getMessage().getChannel().sendMessage(message).queue( m ->
                        new Thread(new DeleteMessageThread(deleteTimeVerify, m)).start()
                );
            }
            new Thread(new DeleteMessageThread(0, event.getMessage())).start();
        }
    }
}
