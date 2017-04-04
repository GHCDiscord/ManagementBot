package ManagementBot.Content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
        if (isModerator(member))
            sendModeratorHelpMessage(user);
        else
            sendNormalHelpMessage(user);
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
