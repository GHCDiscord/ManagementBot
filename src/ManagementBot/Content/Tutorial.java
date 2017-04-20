package ManagementBot.Content;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.List;

import static ManagementBot.Content.Content.isModerator;

public class Tutorial extends Guide implements Command {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            event.getMessage().deleteMessage().queue();
        } catch (PermissionException e) {
            if (event.getChannel().getType() != ChannelType.PRIVATE) //Private Nachrichten können nicht gelöscht werden
                e.printStackTrace();
        }
        if (isModerator(event.getMember())) {
            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            MessageBuilder builder = new MessageBuilder();
            mentionedUsers.forEach(builder::append);
            builder.append("\n").append(faq);
            event.getTextChannel().sendMessage(builder.build()).queue();
        }
    }
}
