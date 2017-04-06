package de.ghc.managementbot.content;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import static de.ghc.managementbot.content.Content.addRole;
import static de.ghc.managementbot.content.Content.deleteTimeVerify;

public class Verify extends Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getGuild() != null) {
            if (!event.getMember().getRoles().contains(event.getGuild().getRolesByName("verified", true).get(0))) {
                addRole(event.getMember(), event.getGuild(), event.getGuild().getRolesByName("verified", true).get(0));
                Message message = new MessageBuilder().append(event.getAuthor()).append(" ist nun verifiziert!").build();
                event.getMessage().getChannel().sendMessage(message).queue(m ->
                        new Thread(new DeleteMessageThread(deleteTimeVerify, m)).start()
                );

            } else {
                Message message = new MessageBuilder().append(event.getAuthor()).append(" ist bereits verifiziert").build();
                event.getMessage().getChannel().sendMessage(message).queue(m ->
                        new Thread(new DeleteMessageThread(deleteTimeVerify, m)).start()
                );
            }
            try {
//                event.getMessage().deleteMessage().queue();
            } catch (PermissionException e) {
                if (event.getChannel().getType() != ChannelType.PRIVATE)  //Private Nachrichten können nicht gelöscht werden
                    e.printStackTrace();
            }
        }
    }
}
