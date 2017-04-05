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
import java.util.*;
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

    static final String faq = "Informationen und Erklärungen zum Spiel und seiner Funktionsweise findest du unter https://docs.google.com/document/d/18h_Ik023Ax9eGUxSCzVszhTask1y5ayP2TweVFNMdHE/pub";

    private static Map<User, Command> userAddIPWithQuestionsMap = new HashMap<>();

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

    public static Map<User, Command> getUserAddIPWithQuestionsMap() {
        return userAddIPWithQuestionsMap;
    }
    public static void addUser (User user, AddIPWithQuestions command) {
      userAddIPWithQuestionsMap.put(user, command);
    }

    public static void deleteUser (User user, AddIPWithQuestions command) {
        userAddIPWithQuestionsMap.remove(user, command);
    }
}
