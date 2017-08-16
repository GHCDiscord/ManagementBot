package de.ghc.managementbot.content;

import com.google.api.client.util.DateTime;
import de.ghc.managementbot.commands.UpdateIP;
import net.dv8tion.jda.core.JDA;
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
import java.util.*;
import java.util.List;

public class Content {

    public static final String GHCImageURL = "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200";

    private static Map<User, AddIP> userAddIP = new HashMap<>();

    private static Map<User, UpdateIP> userUpdateIP = new HashMap<>();

    private static Guild ghc;
    private static JDA jda;

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
            guild.getController().addSingleRoleToMember(member, role).complete();
        } catch (PermissionException e) {
            ghc.getTextChannelById(Data.Channel.botLog).sendMessageFormat("Failed to add role %s to Member %s on guild %s \n reason: %s", role.getName(), member.getEffectiveName(), guild.getName(), e.toString()).queue();
        }
    }

    public static boolean isModerator(Member member) {
        if (member == null)
            return false;
        if (member.getGuild().getIdLong() != Data.Guild.GHC)
            return false;
        List<Role> roles = member.getRoles();
        return roles.contains(getGhc().getRoleById(Data.Role.botMod));
    }

    public static boolean isVerified(Member member) {
        if (member == null)
            return false;
        if (member.getGuild().getIdLong() != Data.Guild.GHC)
            return false;
        List<Role> roles = member.getRoles();
        return roles.contains(getGhc().getRoleById(Data.Role.verified));
    }

    public static Member getGHCMember(User user) {
        if (getGhc() != null) {
            return getGhc().getMember(user);
        }
        return null;
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }

    public static String formatDate() {
        return formatDate(new Date());
    }

    public static String formatDate(DateTime oldDate) {
        return formatDate(new Date(oldDate.getValue()));
    }

    public static void sendException(Throwable t, Class<?> at) {
        getGhc().getTextChannelById(Data.Channel.botLog).sendMessageFormat("%s: %s: %s", at.getName(), t.getClass().getName(), t.getLocalizedMessage()).queue();
    }

    public static boolean isYes(String msg) {
        return msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("Yes") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y") || msg.equalsIgnoreCase("jo");
    }

    public static synchronized void setGhc(Guild ghc) {
        Content.ghc = ghc;
    }

    public static synchronized Guild getGhc() {
        return ghc;
    }

    public static JDA getJda() {
        return jda;
    }

    public static void setJda(JDA jda) {
        Content.jda = jda;
    }

    public static Map<User, AddIP> getUserAddIP() {
        return userAddIP;
    }
    public static void addUserAddIP(User user, AddIP addIP) {
        userAddIP.put(user, addIP);
    }
    public static void deleteUserAddIP(User user, AddIP addIP) {
        userAddIP.remove(user, addIP);
    }

    public static Map<User, UpdateIP> getUpdateIP() {
        return userUpdateIP;
    }

    public static void addUserUpdateIP(User user, UpdateIP updateIP) {
        userUpdateIP.put(user, updateIP);
    }
    public static void deleteUserUpdateIP(User user, UpdateIP update) {
        userUpdateIP.remove(user, update);
    }
}
