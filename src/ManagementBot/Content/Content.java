package ManagementBot.Content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Content {

    //Versionsnumer
    public static final int VersionNumer = 2;
    //Version
    public static final String Version = "Stable 2";

    static final String helpMessageIntro = "**GHC Bot**\n" +
            "Dies ist der offizielle Bot der German Hackers Community (GHC). Er verfügt über diese Befehle:";

    static final String helpMessageUserCommands = "**!stats**: Zeigt live-Statistiken des Spiels an. Sie werden täglich zurückgesetzt.\n"
            + "**!topguilds**: Zeigt die besten 10 Gilden an";
    static final String helpMessageModCommands = "\n**!tut + @User** oder **!guide + @User**: Zeigt einem Nutzer den Link zum Tutorial *Nur für Moderatoren*\n" +
            "**!regeln + @User** oder **!rules + @User**: Sagt einem Nutzer, er solle sich die Regeln durchlesen *Nur für Moderatoren*";

    static final String helpMessageVerified = "Der Bot kümmert sich auch um die Vergabe des Rangs Verified. \n" +
            "Solltest du noch nicht den Verifeid-Rang erreicht haben, lese dir bitte die Regeln nochmal genau durch.\n" +
            "**Dieser Rang wird nicht vom GHC-Team vergeben! Nachrichten an die Mods sind wirkungslos!**";

    static final int deleteTimeVerify = 600;

    public static void startCommand(MessageReceivedEvent event) {
        getCommand(event).onMessageReceived(event);
    }

    private static Command getCommand(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent();
        String[] command = msg.split(" ");

        if (msg.equalsIgnoreCase("!stats")) {
            return new Stats();
        } else if (msg.equalsIgnoreCase(".c3po")) { //verify
            return new Verify();
        } else if (msg.equalsIgnoreCase("!topGuilds")) {
            return new TopGuilds();
        } else if (msg.equalsIgnoreCase("!help")) {
            return new Help();
        } else if (command[0].equalsIgnoreCase("!regeln") || command[0].equalsIgnoreCase("!rules")) {
            return new Rules();
        } else if (command[0].equalsIgnoreCase("!tut") || command[0].equalsIgnoreCase("!guide")) {
            return new Tutorial();
        } else if (command[0].equalsIgnoreCase("!addip") && command.length > 3) {
            return new AddIP();
        }
        return new Command() {
            @Override
            void onMessageReceived(MessageReceivedEvent event) {

            }
        };
    }

    public static Color getRandomColor()  {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    static void addRole(Member member, Guild guild, Role role) {
        try {
            guild.getController().addRolesToMember(member, role).queue();
        } catch (PermissionException e) {
            e.printStackTrace();
        }
    }
    public static Message getJoinMessage(GuildMemberJoinEvent event) {
        return new MessageBuilder().append("Willkommen, ")
                .append(event.getMember())
                .append(". Bevor du uns Fragen vorlegst, wirf bitte unbedingt zunächst einen Blick in die ")
                .append(event.getGuild().getTextChannelsByName("regeln", true).get(0))
                .append("\nViel Spaß bei der GHC | German Hackers Community")
                .build();
    }

    static boolean isModerator(Member member) {
        if (member == null)
            return false;
        List<Role> roles = member.getRoles();
        return roles.containsAll(member.getGuild().getRolesByName("GHC-Staff", true))
                || roles.containsAll(member.getGuild().getRolesByName("Admin", true))
                || roles.containsAll(member.getGuild().getRolesByName("Moderator", true))
                || roles.containsAll(member.getGuild().getRolesByName("Hackers-Staff", true))
                || roles.containsAll(member.getGuild().getRolesByName("Coding", true))
                || roles.containsAll(member.getGuild().getRolesByName("Sponsor", true))
                || roles.containsAll(member.getGuild().getRolesByName("Ex-Staff", true));
    }

    static boolean checkIP(String s) {
        String[] numbers = s.split("\\.");
        if (numbers.length != 4)
            return false;
        try {
            for (String strg : numbers) {
                Integer.parseInt(strg);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
