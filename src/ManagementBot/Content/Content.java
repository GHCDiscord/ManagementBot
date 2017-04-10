package ManagementBot.Content;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Content {

    //Versionsnumer
    public static final int VersionNumer = 2;
    //Version
    public static final String Version = "Stable 2";

    static final String helpMessageIntro = "**GHC Bot**\n" +
            "Dies ist der offizielle Bot der German Hackers Community (GHC). Er verfügt über diese Befehle:";

    static final String helpMessageUserCommands = "**!stats**: Zeigt live-Statistiken des Spiels an. Sie werden täglich zurückgesetzt.\n"
            + "**!topguilds**: Zeigt die besten 10 Gilden an" +
            "**!addIP**: Fügt eine IP der IP-Datenbank hizu. Für weitere Informationen schreibe *!help addIP";
    static final String helpMessageModCommands = "\n**!tut + @User** oder **!guide + @User**: Zeigt einem Nutzer den Link zum Tutorial *Nur für Moderatoren*\n" +
            "**!regeln + @User** oder **!rules + @User**: Sagt einem Nutzer, er solle sich die Regeln durchlesen *Nur für Moderatoren*\n" +
            "**!gilde + @User** oder **!guild + @User**: Zeit einen Nutzer den Link zum Giden-Tutorial im Forum *Nur für Moderatoren*";

    static final String helpMessageVerified = "Der Bot kümmert sich auch um die Vergabe des Rangs Verified. \n" +
            "Solltest du noch nicht den Verifeid-Rang erreicht haben, lese dir bitte die Regeln nochmal genau durch.\n" +
            "**Dieser Rang wird nicht vom GHC-Team vergeben! Nachrichten an die Mods sind wirkungslos!**";

    static final String helMessageAddIPParams = "**!addIP IP** Als erstes muss eine *gültige* IP angegeben werden.\n" +
            "Darauf können einige dieser Parameter folgen: \n" +
            "**-n** Name des Hackers (nur ein Wort)\n" +
            "**-m** Anzahl der Miner" +
            "**-r** Repopulation des Hackers\n" +
            "**-g** Kürzel der Gilde des Hackers. (immer drei Zeichen) \n" +
            "Alle darauf folgenden Wörter werden automatisch der Beschreibung hinzugefügt\n" +
            "Wenn keine Parameter angegeben werden, werden die nötigen Informationen durch den Bot abgefragt.";

    static final int deleteTimeVerify = 600;

    static final String faq = "Informationen und Erklärungen zum Spiel und seiner Funktionsweise findest du unter https://docs.google.com/document/d/18h_Ik023Ax9eGUxSCzVszhTask1y5ayP2TweVFNMdHE/pub";

    static final String guild = "Alle wichtigen Informationen zu Gilden und deren Funktionsweise findest du unter http://forum.hackerz.online/viewtopic.php?f=12&t=78";

    private static Map<User, Command> userAddIPWithQuestionsMap = new HashMap<>();

    private static Map<User, Command> userAddIPsWithParamsMap = new HashMap<>();

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
    public static void addUserAddIPWithQuestions (User user, AddIPWithQuestions command) {
      userAddIPWithQuestionsMap.put(user, command);
    }

    public static void deleteUserAddIPWithQuestions (User user, AddIPWithQuestions command) {
        userAddIPWithQuestionsMap.remove(user, command);
    }
    public static void addUserAddIPWithParams(User user, AddIPsWithParams command) {
        userAddIPsWithParamsMap.put(user, command);
    }
    public static void deleteUserAddIPWithParams (User user, AddIPsWithParams command) {
        userAddIPsWithParamsMap.remove(user, command);
    }

    public static Map<User, Command> getUserAddIPsWithParamsMap() {
        return userAddIPsWithParamsMap;
    }
}
