package ManagementBot.Content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

import static ManagementBot.Content.Content.*;

public class Help extends Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Member member = event.getMember();
        String[] command = event.getMessage().getContent().split(" ");
        List<User> mentionedUsers = event.getMessage().getMentionedUsers();

        if (user == null)
            return;
        if (member != null && !member.getUser().equals(user))
            throw new IllegalArgumentException("User and Member have to be the same User!");

        if (event.getGuild() != null) {
            if (mentionedUsers.isEmpty()) {
                event.getTextChannel().sendMessage(
                        new MessageBuilder().append(user).append(" ich habe dir alle wichtigen Informationen als private Nachricht gesendet!").build()
                ).queue(m ->
                        new Thread(new DeleteMessageThread(60, m)).start()
                );
            } else {
                MessageBuilder builder = new MessageBuilder();
                mentionedUsers.forEach(builder::append);
                event.getTextChannel().sendMessage(builder.append(" ich habe dir alle wichtigen Informationen als private Nachricht gesendet!").build()).queue( m ->
                        new Thread(new DeleteMessageThread(60, m)).start()
                );
            }
        }


        if (command.length > 1 && command[1].equalsIgnoreCase("addIP"))
            if (mentionedUsers.isEmpty())
                sendAddIPHelpMessage(user);
            else
                mentionedUsers.forEach(Help::sendAddIPHelpMessage);
         else {
            if (mentionedUsers.isEmpty()) {
                if (isModerator(member))
                    sendModeratorHelpMessage(user);
                else
                    sendNormalHelpMessage(user);
            } else
            mentionedUsers.forEach(u -> {
                if (event.getGuild() != null && isModerator(event.getGuild().getMember(u)))
                    sendModeratorHelpMessage(u);
                else
                    sendNormalHelpMessage(u);
            });

        }
        if (event.getGuild() != null)
           new Thread(new DeleteMessageThread(0, event.getMessage())).start();


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
    private static void sendAddIPHelpMessage(User user) {
        user.openPrivateChannel().queue(DM ->
                DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helMessageAddIPParams).build()).queue()
        );
    }
}
