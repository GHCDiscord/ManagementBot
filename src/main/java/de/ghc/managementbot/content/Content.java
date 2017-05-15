package de.ghc.managementbot.content;

import de.ghc.managementbot.commands.AddIPWithQuestions;
import de.ghc.managementbot.commands.AddIPsWithParams;
import de.ghc.managementbot.commands.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Content {

    //Versionsnumer
    public static final int versionNumer = 5;
    //Version
    public static final String version = "Development 3.2.2";

    public static final String GHCImageURL = "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200";

    private static Map<User, Command> userAddIPWithQuestionsMap = new HashMap<>();

    private static Map<User, Command> userAddIPsWithParamsMap = new HashMap<>();

    private static Guild ghc;

    public static Color getRandomColor()  {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static Color getImageColor(String imageURL) {
        try {
            URL url = new URL(imageURL);
            BufferedImage image = ImageIO.read(url);
            Random r = new Random();
            return new Color(image.getRGB(1 + r.nextInt(image.getWidth() - 2),1 + r.nextInt(image.getHeight() - 2)));
        } catch (Exception e) {
            return getRandomColor();
        }
    }

    public static void addRole(Member member, Guild guild, Role role) {
        try {
            guild.getController().addRolesToMember(member, role).queue();
        } catch (PermissionException e) {
            e.printStackTrace();
        }
    }

    public static boolean isModerator(Member member) {
        if (member == null)
            return false;
        List<Role> roles = member.getRoles();
        return     roles.containsAll(member.getGuild().getRolesByName("Admin", true))
                || roles.containsAll(member.getGuild().getRolesByName("Moderator", true))
                || roles.containsAll(member.getGuild().getRolesByName("Hackers-Staff", true))
                || roles.containsAll(member.getGuild().getRolesByName("Coding", true))
                || roles.containsAll(member.getGuild().getRolesByName("Sponsor", true))
                || roles.containsAll(member.getGuild().getRolesByName("Ex-Staff", true))
                || roles.containsAll(member.getGuild().getRolesByName("Autor", true));
    }

    public static boolean isVerified(Member member) {
        if (member == null)
            return false;
        List<Role> roles = member.getRoles();
        return roles.containsAll(member.getGuild().getRolesByName("Verified", true));
    }
    public static Member getGHCMember(User user) {
        if (ghc != null) {
            return ghc.getMember(user);
        }
        return null;
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }

    public static String formatDate() {
        return formatDate(new Date());
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
