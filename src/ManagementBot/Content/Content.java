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
import java.util.Date;
import java.util.List;

public class Content {

    //Versionsnumer
    public static final int VersionNumer = 2;
    //Version
    public static final String Version = "Development 1.1.04";

    private static final String helpMessageIntro = "**GHC Bot**\n" +
            "Dies ist der offizielle Bot der German Hackers Community (GHC). Er verfügt über diese Befehle:";

    private static final String helpMessageCommans = "**!stats**: Zeigt live-Statistiken des Spiels an. Sie werden täglich zurückgesetzt.\n"
            + "**!topguilds**: Zeigt die besten 10 Gilden an";

    private static final String helpMessageVerified = "Der Bot kümmert sich auch um die Vergabe des Rangs Verified. \n" +
            "Solltest du noch nicht den Verifeid-Rang erreicht haben, lese dir bitte die Regeln nochmal genau durch.\n" +
            "**Dieser Rang wird nicht vom GHC-Team vergeben! Nachrichten an die Mods sind wirkungslos!**";

    public static void sendhelpMessage(User user) {
        user.openPrivateChannel().queue(DM -> {
            DM.sendMessage(helpMessageIntro).queue();
            DM.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageCommans).build()).queue();
            DM.sendMessage(helpMessageVerified).queue();
        });
    }

    public static Message getStats() {
        try {
            URL url = new URL("http://hackerz.online/stats.json");
            String s = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
            JSONObject jsonObject = new JSONObject(s).getJSONObject("game");
            /*MessageEmbed messageEmbed = new EmbedBuilder()
                    .setTitle("Statistiken")
                    .addField("", "**Blacklist-Einträge**" +  jsonObject.getInt("blacklists") + "", false)
                    .addField("" , "**Fehlgeschlagene Bot-Attaken**"+ jsonObject.getInt("bot_attacks_failed") +  "", false)
                    .addField("" ,"**Erfolgreiche Bot-Attaken**"+ jsonObject.getInt("bot_attacks_success") + "", true)
                    .addField("" ,"**Verbindungen**"+ jsonObject.getInt("connections_to_target") + "", false)
                    .addField("" ,"**Erfolgreich geknackte Passwörter**"+ jsonObject.getInt("successful_cracked_passwords") + "", true)
                    .addField("", "**Gestohlene Miner**"+ jsonObject.getInt("total_miners_stolen") + "", false)
                    .addField("", "**Gestohlene Wallets**"+ jsonObject.getInt("total_wallets_stolen") + "", true)
                    .setFooter("Stand: " + new Date(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200")
                    .build();
            return new MessageBuilder().setEmbed(messageEmbed).build(); */
            String strg = new StringBuilder()
                    .append("**Blacklist-Einträge:** ").append(jsonObject.getInt("blacklists"))
                    .append("\n**Fehlgeschlagene Bot-Attaken:** ").append(jsonObject.getInt("bot_attacks_failed"))
                    .append("\n**Erfolgreiche Bot-Attaken:** ").append(jsonObject.getInt("bot_attacks_success"))
                    .append("\n**Verbindungen:** ").append(jsonObject.getInt("connections_to_target"))
                    .append("\n**Erfolgreich geknackte Passwörter:** ").append(jsonObject.getInt("successful_cracked_passwords"))
                    .append("\n**Gestohlene Miner:** ").append(jsonObject.getInt("total_miners_stolen"))
                    .append("\n**Gestohlene Wallets:** ").append(jsonObject.getInt("total_miners_stolen"))
                    .toString();
            return new MessageBuilder().setEmbed(new EmbedBuilder().setTitle("Statistiken").setColor(getRandomColor())
                    .setDescription(strg)
                    .setFooter("Stand: " + new Date(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200").build()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MessageBuilder().build();
    }

    private static Color getRandomColor()  {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static Message getTopGuilds() {
        try {
            URL url = new URL("http://hackerz.online/stats.json");
            String st = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
            JSONArray jsonArray = new JSONObject(st).getJSONArray("top_20_guilds");
            EmbedBuilder builder = new EmbedBuilder().setTitle("Top 10 Gilden:").setColor(getRandomColor());

            //Funktioniert nicht auf mobilen Geräten

            //StringBuilder ranks = new StringBuilder(), names = new StringBuilder("**"), mitigation = new StringBuilder();
            /*for (int i = 0; i < jsonArray.length(); i++) {
                ranks.append(i+1).append("\n");
                names.append(jsonArray.getJSONObject(i).getString("guild_name")).append("\n");
                mitigation.append(jsonArray.getJSONObject(i).getString("mitigation")).append("\n");
            }
            names.append("**");
            builder.addField("Rank", ranks.toString(), true)
                    .addField("Name", names.toString(), true)
                    .addField("Mitigation", mitigation.toString(), true)
                    .setFooter("Stand: " + new Date(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200");
            return new MessageBuilder().setEmbed(builder.build()).build(); */

            StringBuilder string = new StringBuilder();
            for (int i = 0; (i < jsonArray.length() && i < 10); i++) {
                string.append(i+1)
                        .append(". **")
                        .append(jsonArray.getJSONObject(i).getString("guild_name"))
                        .append(" **")
                        .append(jsonArray.getJSONObject(i).getString("mitigation"))
                        .append("\n");
            }
            return new MessageBuilder().setEmbed( builder.setDescription(string.toString()).build()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new MessageBuilder().build();
    }

    private static final int deleteTimeVerify = 600;

    public static void verify(MessageReceivedEvent event) {
        if (event.getGuild() != null) {
            if (!event.getMember().getRoles().contains(event.getGuild().getRolesByName("verified", true).get(0))) {
                Content.addRole(event.getMember(), event.getGuild(), event.getGuild().getRolesByName("verified", true).get(0));
                Message message = new MessageBuilder().append(event.getAuthor()).append(" ist nun verifiziert!").build();
                event.getMessage().getChannel().sendMessage(message).queue( m ->
                        new Thread(new DeleteMessageThread(deleteTimeVerify, m)).start()
                );

            } else {
                Message message = new MessageBuilder().append(event.getAuthor()).append(" ist bereits verifiziert").build();
                event.getMessage().getChannel().sendMessage(message).queue( m ->
                        new Thread(new DeleteMessageThread(deleteTimeVerify, m)).start()
                );
            }
            event.getMessage().deleteMessage().queue();
        }
    }

    public static void addRole(Member member, Guild guild, Role role) {
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
    public static void rules(MessageReceivedEvent event) {
        List<Role> roles = event.getMember().getRoles();
        event.getMessage().deleteMessage().queue();
        if (roles.containsAll(event.getGuild().getRolesByName("GHC-Staff", true))
                || roles.containsAll(event.getGuild().getRolesByName("Admin", true))
                || roles.containsAll(event.getGuild().getRolesByName("Moderator", true))
                || roles.containsAll(event.getGuild().getRolesByName("Hackers-Staff", true))
                || roles.containsAll(event.getGuild().getRolesByName("Coding", true))
                || roles.containsAll(event.getGuild().getRolesByName("Sponsor", true))
                || roles.containsAll(event.getGuild().getRolesByName("Ex-Staff", true))
                ) {
            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            MessageBuilder builder = new MessageBuilder();
            mentionedUsers.forEach(builder::append);
            builder.append(" lies dir bitte die ")
                    .append(event.getGuild().getTextChannelsByName("regeln", true).get(0))
                    .append(" genau durch!");
            event.getTextChannel().sendMessage(builder.build()).queue();
        }
    }

    private static final String faq = "Informationen und Erklärungen zum Spiel und seiner Funktionsweise findest du unter https://docs.google.com/document/d/18h_Ik023Ax9eGUxSCzVszhTask1y5ayP2TweVFNMdHE/pub";

    public static void tutorial(MessageReceivedEvent event) {
        List<Role> roles = event.getMember().getRoles();
        event.getMessage().deleteMessage().queue();
        if (roles.containsAll(event.getGuild().getRolesByName("GHC-Staff", true))
                || roles.containsAll(event.getGuild().getRolesByName("Admin", true))
                || roles.containsAll(event.getGuild().getRolesByName("Moderator", true))
                || roles.containsAll(event.getGuild().getRolesByName("Hackers-Staff", true))
                || roles.containsAll(event.getGuild().getRolesByName("Coding", true))
                || roles.containsAll(event.getGuild().getRolesByName("Sponsor", true))
                || roles.containsAll(event.getGuild().getRolesByName("Ex-Staff", true))
                ) {
            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            MessageBuilder builder = new MessageBuilder();
            mentionedUsers.forEach(builder::append);
            builder.append("\n").append(faq);
            event.getTextChannel().sendMessage(builder.build()).queue();
        }
    }

}
