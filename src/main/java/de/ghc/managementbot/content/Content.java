package de.ghc.managementbot.content;

import com.google.api.client.util.DateTime;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.Language;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static de.ghc.managementbot.entity.Country.*;

public class Content {

    public static final String GHCImageURL = "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200";

    private static Guild ghc;

    public static final Command doNothing = new Command() {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
        }

        @Override
        public List<String> getCallers() {
            return Collections.emptyList();
        }

        @Override
        public Command createCommand(MessageReceivedEvent event) {
            return doNothing;
        }
    };

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
        if (member == null || guild == null || role == null)
            return;
        try {
            guild.getController().addSingleRoleToMember(member, role).complete();
        } catch (PermissionException e) {
            e.printStackTrace();
            //getGhc().getTextChannelById(Data.Channel.botLog).sendMessageFormat("Failed to add role %s to Member %s on guild %s \n reason: %s", role.getName(), member.getEffectiveName(), guild.getName(), e.toString()).queue();
        }
    }

    public static void addRole(Member member, Role role) {
        if (member == null || role == null)
            return;
        if (role.getGuild().getIdLong() == getGhc().getIdLong())
            addRole(member, getGhc(), role);
    }

    public static void addRoles(Member member, Guild guild, List<Role> roles) {
        if (member == null || guild == null || roles == null)
            return;
        if (roles.size() == 1) {
            addRole(member, guild, roles.get(0));
            return;
        }
        try {
            guild.getController().addRolesToMember(member, roles).complete();
        } catch (PermissionException e) {
            getGhc().getTextChannelById(Data.Channel.botLog).sendMessageFormat("Failed to add roles %s to Member %s on guild %s \n reason: %s", Arrays.toString(roles.toArray()), member.getEffectiveName(), guild.getName(), e.toString()).queue();
        }
    }

    public static void addRoles(Member member, List<Role> roles) {
        if (member == null || roles == null)
            return;
        roles.removeIf(r -> r.getGuild().getIdLong() != getGhc().getIdLong());
        addRoles(member, getGhc(), roles);
    }

    public static void removeRole(Member member, Guild guild, Role role) {
        if (member == null || guild == null || role == null)
            return;
        try {
            guild.getController().removeSingleRoleFromMember(member, role).complete();
        } catch (PermissionException e) {
            getGhc().getTextChannelById(Data.Channel.botLog).sendMessageFormat("Failed to remove role %s from Member %s on guild %s \n reason: %s", role.getName(), member.getEffectiveName(), guild.getName(), e.toString()).queue();
        }
    }

    public static void removeRole(Member member, Role role) {
        if (member == null || role == null)
            return;
        if (role.getGuild().getIdLong() == getGhc().getIdLong())
            removeRole(member, getGhc(), role);
    }

    public static void removeRoles(Member member, Guild guild, List<Role> roles) {
        if (member == null || guild == null || roles == null)
            return;
        if (roles.size() == 1) {
            removeRole(member, guild, roles.get(0));
            return;
        }
        try {
            guild.getController().removeRolesFromMember(member, roles).complete();
        } catch (PermissionException e) {
            getGhc().getTextChannelById(Data.Channel.botLog).sendMessageFormat("Failed to remove roles %s from Member %s on guild %s \n reason: %s", Arrays.toString(roles.toArray()), member.getEffectiveName(), guild.getName(), e.toString()).queue();
        }
    }

    public static void removeRoles(Member member, List<Role> roles) {
        if (member == null || roles == null)
            return;
        roles.removeIf(r -> r.getGuild().getIdLong() != getGhc().getIdLong());
        removeRoles(member, getGhc(), roles);
    }

    public static Language getLanguage(TextChannel channel) {
        return getCountry(channel).getLanguage();
    }

    public static de.ghc.managementbot.entity.Country getCountry (TextChannel channel) {
        Category category = channel.getParent();
        if (category == null)
            return GERMANY;
        switch (category.getName()) {
            case "GER | German":
                return GERMANY;
            case "EN | English":
            case "GLO | Global Chat":
            default:
                return ENGLAND;
        }
    }

    public static boolean isBotModerator(Member member) {
        return hasRole(member, Data.Role.botMod);
    }

    public static boolean isVerified(Member member) {
       return ENGLAND.isVerified(member) || GERMANY.isVerified(member);
    }

    public static boolean isStaff(Member member) {
        return ENGLAND.isVerified(member) || GERMANY.isVerified(member);
    }

    public static boolean hasRole(Member member, long roleId) {
        if (member == null)
            return false;
        if (member.getGuild().getIdLong() != Data.Guild.GHC)
            return false;
        List<Role> roles = member.getRoles();
        return roles.contains(member.getGuild().getRoleById(roleId));
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
        getGhc().getTextChannelById(Data.Channel.botLog).sendMessageFormat("%s: %s: %s", at.getSimpleName(), t.getClass().getSimpleName(), t.getLocalizedMessage()).queue();
        if (t.getCause() != null)
            sendException(t.getCause(), at);
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
}
