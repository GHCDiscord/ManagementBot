package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

import static de.ghc.managementbot.content.Content.isBotModerator;

public class Rules implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getGuild() != null)
            event.getMessage().delete().queue();


        if (isBotModerator(event.getMember())) {
            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            MessageBuilder builder = new MessageBuilder();
            mentionedUsers.forEach(builder::append);
            builder.append(" lies dir bitte die <#")
                    .append(Data.Channel.de_regeln)
                    .append("> genau durch!");
            event.getTextChannel().sendMessage(builder.build()).queue();
        }
    }

    @Override
    public List<String> getCallers() {
        return Arrays.asList("!regeln", "!rules");
    }

    @Override
    public boolean isCalled(String msg) {
        return isCalledFirstWord(msg);
    }
}
