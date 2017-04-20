package de.ghc.managementbot.content;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static de.ghc.managementbot.content.Content.isModerator;

public class GuildsGuide extends Guide implements Command {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (isModerator(event.getMember())) {
            MessageBuilder builder = new MessageBuilder();
            event.getMessage().getMentionedUsers().forEach(builder::append);
            event.getChannel().sendMessage(builder.append(guild).build()).queue();
        }
       new Thread(new DeleteMessageThread(0, event.getMessage())).start();
    }
}
