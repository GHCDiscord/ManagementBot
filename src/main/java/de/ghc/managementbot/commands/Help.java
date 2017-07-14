package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

import static de.ghc.managementbot.content.Content.getRandomColor;
import static de.ghc.managementbot.content.Content.isModerator;
import static de.ghc.managementbot.content.Content.isVerified;

public class Help implements Command {

    private static final String helpMessageIntro = "**GHC Bot**\n" +
            "Dies ist der offizielle Bot der German Hackers Community (GHC). Er verf\u00FCgt \u00FCber diese Befehle:";

    private static final String helpMessageUserCommands = "**!stats**: Zeigt live-Statistiken des Spiels an. Sie werden t\u00E4glich zur\u00FCckgesetzt.\n" +
            "**!stats *Land***: Zeit live-Statistiken eine bestimmten Landes an\nM\u00F6glich ist entweder der Landescode (z.B. 'DE' f\u00FCr Deutschland, 'ES' f\u00FCr Spanien)\noder der englische Name des Landes (z.B. Germany, Espain)"
            + "\n**!topguilds**: Zeigt die besten 10 Gilden an\n" +
            "**!topcountry**: Zeigt die besten 10 L\u00E4nder an\n" +
            "**!stats DB** Zeigt aktuelle Statistiken der IP-Datenbank an\n" +
            "**!allTut** Zeigt die Links zu allen GHC-Tutorials an\n" +
            "**!version** Zeigt die aktuelle Version des Spiels an";

    private static final String helpMessageVerifiedCommands =
            "\n\n**Nachfolgende Befehle funktionieren ausschlie\u00dflich im Channel #hackers-ip:**\n\n" +
            "**!addIP**: F\u00FCgt eine IP der IP-Datenbank hinzu. F\u00FCr weitere Informationen schreibe **!help addIP**\n" +
            "**!register + *Nutzername im Spiel***: Erstellt einen neuen, f\u00FCr 30 Tage g\u00FCltigen Account in der GHC-IP-Datenbank\n" +
            "**!refresh**: Reaktiviert deinen Account in der GHC-IP-Datenbank wieder, wenn er abgelaufen ist.";
    private static final String helpMessageModCommands =
            "\n\n**Nachfolgende Befehle stehen nur Teammitgliedern zur Verf\u00FCgung**\n\n" +
            "**!tut *@User*** oder **!guide *@User*  **: Zeigt einem Nutzer den Link zum Tutorial \n" +
            "**!regeln *@User*** oder **!rules *@User***: Sagt einem Nutzer, er solle sich die Regeln durchlesen \n" +
            "**!gilde *@User*** oder **!guild *@User***: Zeit einem Nutzer den Link zum Giden-Tutorial im Forum \n" +
            "**!taktik *@User***: Zeigt einem Nutzer den Link zum Taktik-Tutorial von Doc\n" +
            "**!en *@User*** oder **!de *@User***: Sagt englischsprachigen Nutzern, sie sollen den englischen Discord verwenden \n" +
            "**!help *@User***:: Sendet einem Nutzer diesen Text";

    private static final String helpMessageVerified = "Der Bot k\u00FCmmert sich auch um die Vergabe des Rangs Verified. \n" +
            "Solltest du noch nicht den Verified-Rang erreicht haben, lese dir bitte die Regeln nochmal genau durch.\n" +
            "Nach Erhalt dieses Ranges hast du unter andern Zugriff auf unsere IP-Datenbank\n" +
            "**Dieser Rang wird nicht vom GHC-Team vergeben! Nachrichten an die Mods sind wirkungslos!**";

    private static final String helMessageAddIPParams = "Es gibt drei M\u00F6glichkeiten, eine IP zur Datenbank hinzuzuf\u00FCgen:\n" +
            "\n1. Parameter:\n" +
            "Schreibe **!addIP** gefolgt von einer g\u00FCltigen IP-Adresse.\n" +
            "Darauf k\u00F6nnen diese Parameter folgen:\n" +
            "**-n** Name des Hackers (nur ein Wort)\n" +
            "**-m** Anzahl der Miner\n" +
            "**-r** Reputation des Hackers\n" +
            "**-g** K\u00FCrzel der Gilde des Hackers. (immer drei oder vier Zeichen) \n" +
            "Alle darauf folgenden W\u00F6rter werden automatisch der Beschreibung hinzugef\u00FCgt\n" +
            "\nBeispiele:\n" +
            "**!addIP 1.2.3.4 -n {Datacenter ABC} -g ABC -m 0**\n" +
            "**!addIP 4.3.2.1 -n HackerTom -r 1234 -m 2 Sehr aktiv**\n" +
            "\n2. Reihenfolge\n" +
            "Es geht etwas schneller, wenn man die Werte ohne Parameter eingibt.\n" +
            "Dann m\u00FCssen sie in dieser Reihenfolge sein:\n" +
            "**!addIP *IP* *Name* *Rep* *Miner* *Gilde***\n" +
            "Beispiel:\n" +
            "**!addIP 1.2.3.4 HackerTom 1234 2 Sehr aktiv**\n" +
            "\n3. Abfrage\n" +
            "Wenn keine weiteren Informationen angegeben werden, werden die n\u00F6tigen Informationen abgefragt.";

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
           event.getMessage().delete().queue();

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
    }

    private static void sendNewHelpMessage(User user) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageUserCommands).setAuthor("GHC Bot", null, Content.GHCImageURL).build()).queue();
            DM.sendMessage(helpMessageVerified).queue();
        });
    }

    private static void sendNormalHelpMessage(User user) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageUserCommands + helpMessageVerifiedCommands).setAuthor("GHC Bot", null, Content.GHCImageURL).build()).queue();
        });
    }

    private static void sendModeratorHelpMessage(User user) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageUserCommands + helpMessageVerifiedCommands + helpMessageModCommands).setAuthor("GHC Bot", null, Content.GHCImageURL).build()).queue();
        });
    }
    private static void sendAddIPHelpMessage(User user) {
        user.openPrivateChannel().queue(DM ->
                DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helMessageAddIPParams).build()).queue()
        );
    }
}
