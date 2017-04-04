package ManagementBot.Content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import static ManagementBot.Content.Content.*;

public class Help extends Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Member member = event.getMember();

        if (user == null)
            return;
        if (member != null && !member.getUser().equals(user)) {
            throw new IllegalArgumentException("User and Member have to be the same User!");
        }
        event.getTextChannel().sendMessage(
                new MessageBuilder().append(user).append(" ich habe dir alle wichtigen Informationen als private Nachricht gesendet!").build()
        ).queue( m -> {
            if (event.getGuild() != null)
                new Thread(new DeleteMessageThread(60, m)).start();
        });
        if (isModerator(member))
            sendModeratorHelpMessage(user);
        else
            sendNormalHelpMessage(user);
        try {
            event.getMessage().deleteMessage().queue();
        } catch (PermissionException e) {
            if (event.getChannel().getType() != ChannelType.PRIVATE)  //Private Nachrichten können nicht gelöscht werden
                e.printStackTrace();
        }
    }

    private static void sendNormalHelpMessage(User user) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(helpMessageIntro).queue();
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageUserCommands).build()).queue();
            DM.sendMessage(helpMessageVerified).queue();
        });
    }

    private static void sendModeratorHelpMessage(User user ) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(helpMessageIntro).queue();
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageUserCommands + helpMessageModCommands).build()).queue();
            DM.sendMessage(helpMessageVerified).queue();
        });
    }
}
