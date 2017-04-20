package de.ghc.managementbot.content;

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
    public static final int versionNumer = 5;
    //Version
    public static final String version = "Development 2.3";

    private static Map<User, Command> userAddIPWithQuestionsMap = new HashMap<>();

    private static Map<User, Command> userAddIPsWithParamsMap = new HashMap<>();

    private static Guild ghc;

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

    static boolean isVerified(Member member) {
        if (member == null)
            return false;
        List<Role> roles = member.getRoles();
        return roles.containsAll(member.getGuild().getRolesByName("Verified", true));
    }
    static Member getGHCMember(User user) {
        if (ghc != null) {
            return ghc.getMember(user);
        }
        return null;
    }

    public static void setGhc(Guild ghc) {
        Content.ghc = ghc;
    }

    public static Guild getGhc() {
        return ghc;
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
