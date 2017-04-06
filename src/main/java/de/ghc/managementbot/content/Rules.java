package de.ghc.managementbot.content;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

import static de.ghc.managementbot.content.Content.isModerator;

public class Rules extends Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getGuild() != null)
            new Thread(new DeleteMessageThread(0, event.getMessage())).start();


        if (isModerator(event.getMember())) {
            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            MessageBuilder builder = new MessageBuilder();
            mentionedUsers.forEach(builder::append);
            builder.append(" lies dir bitte die ")
                    .append(event.getGuild().getTextChannelsByName("regeln", true).get(0))
                    .append(" genau durch!");
            event.getTextChannel().sendMessage(builder.build()).queue();
        }
    }
}
