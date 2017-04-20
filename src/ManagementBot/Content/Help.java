package ManagementBot.Content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

import static ManagementBot.Content.Content.*;

public class Help implements Command {

    private static final String helpMessageIntro = "**GHC Bot**\n" +
            "Dies ist der offizielle Bot der German Hackers Community (GHC). Er verfügt über diese Befehle:";

    private static final String helpMessageUserCommands = "**!stats**: Zeigt live-Statistiken des Spiels an. Sie werden täglich zurückgesetzt.\n" +
            "**!stats *Land***: Zeit live-Statistiken eine bestimmten Landes an\nMöglich ist entweder der Landescode (z.B. 'DE' für Deutschland, 'ES' für Spanien)\noder der Englische Name des Landes (z.B. Germany, Espain)"
            + "\n**!topguilds**: Zeigt die besten 10 Gilden an\n" +
            "**!topcountry**: Zeigt die besten 10 Länder an";

    private static final String helpMessageVerifiedCommands = "\n**!addIP**: Fügt eine IP der IP-Datenbank hinzu. Für weitere Informationen schreibe **!help addIP**\n" +
            "**!register + *[Nutzername im Spiel]***: Erstellt einen neuen, für 30 Tage gültigen Account in der GHC-IP-Datenbank\n" +
            "**!refresh**: Reaktiviert deinen Account in der GHC-IP-Datenbank wieder, wenn er abgelaufen ist.";
    private static final String helpMessageModCommands = "\n**!tut + *[@User]*** oder **!guide + *[@User]*  **: Zeigt einem Nutzer den Link zum Tutorial *Nur für Moderatoren*\n" +
            "**!regeln + *[@User]*** oder **!rules + *[@User]***: Sagt einem Nutzer, er solle sich die Regeln durchlesen *Nur für Moderatoren*\n" +
            "**!gilde + *[@User]*** oder **!guild + *[@User]***: Zeit einem Nutzer den Link zum Giden-Tutorial im Forum *Nur für Moderatoren*\n" +
            "**!help + *[@User]***:: Sendet einem Nutzer diesen Text";

    private static final String helpMessageVerified = "Der Bot kümmert sich auch um die Vergabe des Rangs Verified. \n" +
            "Solltest du noch nicht den Verified-Rang erreicht haben, lese dir bitte die Regeln nochmal genau durch.\n" +
            "**Dieser Rang wird nicht vom GHC-Team vergeben! Nachrichten an die Mods sind wirkungslos!**";

    private static final String helMessageAddIPParams = "**!addIP IP** Als erstes muss eine *gültige* IP angegeben werden.\n" +
            "Darauf können einige dieser Parameter folgen: \n" +
            "**-n** Name des Hackers (nur ein Wort)\n" +
            "**-m** Anzahl der Miner\n" +
            "**-r** Reputation des Hackers\n" +
            "**-g** Kürzel der Gilde des Hackers. (immer drei Zeichen) \n" +
            "Alle darauf folgenden Wörter werden automatisch der Beschreibung hinzugefügt\n" +
            "Wenn keine Parameter angegeben werden, werden die nötigen Informationen abgefragt.";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Member member = event.getMember();
        if (member == null)
            member = Content.getGHCMember(event.getAuthor());
        boolean verified = isVerified(member);
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
            } else if (verified) {
                MessageBuilder builder = new MessageBuilder();
                mentionedUsers.forEach(builder::append);
                event.getTextChannel().sendMessage(builder.append(" ich habe dir alle wichtigen Informationen als private Nachricht gesendet!").build()).queue( m ->
                        new Thread(new DeleteMessageThread(60, m)).start()
                );
            }
        }


        if (command.length > 1 && command[1].equalsIgnoreCase("addIP")) {
            if (verified) {
                if (mentionedUsers.isEmpty())
                    sendAddIPHelpMessage(user);
                else
                    mentionedUsers.forEach(Help::sendAddIPHelpMessage);
            }
        } else {
            if (mentionedUsers.isEmpty()) {
                if (isModerator(member))
                    sendModeratorHelpMessage(user);
                else if (isVerified(member))
                    sendNormalHelpMessage(user);
                else
                    sendNewHelpMessage(user);
            } else if (verified)
                mentionedUsers.forEach(u -> {
                    if (event.getGuild() != null && isModerator(event.getGuild().getMember(u)))
                        sendModeratorHelpMessage(u);
                    else if (event.getGuild() != null && isVerified(event.getGuild().getMember(u)))
                        sendNormalHelpMessage(u);
                    else
                        sendNewHelpMessage(u);
                });
        }
        if (event.getGuild() != null)
           new Thread(new DeleteMessageThread(0, event.getMessage())).start();


    }

    private static void sendNewHelpMessage(User user) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(helpMessageIntro).queue();
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageUserCommands).build()).queue();
            DM.sendMessage(helpMessageVerified).queue();
        });
    }

    private static void sendNormalHelpMessage(User user) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(helpMessageIntro).queue();
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageUserCommands + helpMessageVerifiedCommands).build()).queue();
            DM.sendMessage(helpMessageVerified).queue();
        });
    }

    private static void sendModeratorHelpMessage(User user) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(helpMessageIntro).queue();
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageUserCommands + helpMessageVerifiedCommands + helpMessageModCommands).build()).queue();
            DM.sendMessage(helpMessageVerified).queue();
        });
    }
    private static void sendAddIPHelpMessage(User user) {
        user.openPrivateChannel().queue(DM ->
                DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helMessageAddIPParams).build()).queue()
        );
    }
}
