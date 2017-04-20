package de.ghc.managementbot.Content;


import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static ManagementBot.Content.Content.isModerator;

public class Taktik extends Guide implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (isModerator(event.getMember())) {
            MessageBuilder builder = new MessageBuilder();
            event.getMessage().getMentionedUsers().forEach(builder::append);
            event.getChannel().sendMessage(builder.append(taktik).build()).queue();
        }
        new Thread(new DeleteMessageThread(0, event.getMessage())).start();
    }
}
