package ManagementBot.Content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Content {
    public static Message getStats() {
        try {
            URL url = new URL("http://hackerz.online/stats.json");
            String s = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
            JSONObject jsonObject = new JSONObject(s).getJSONObject("game");
            MessageEmbed messageEmbed = new EmbedBuilder()
                    .setTitle("Statistiken")
                    .addField("Blacklist-Einträge:", jsonObject.getInt("blacklists") + "", true)
                    .addField("Fehlgeschlagene Bot-Attaken:", jsonObject.getInt("bot_attacks_failed") +  "", true)
                    .addField("Verbindungen", jsonObject.getInt("connections_to_target") + "", true)
                    .addField("Erfolgreich geknackte Passwörter:", jsonObject.getInt("successful_cracked_passwords") + "", true)
                    .addField("Gestohlene Miner", jsonObject.getInt("total_miners_stolen") + "", true)
                    .addField("Gestohlene Wallets", jsonObject.getInt("total_wallets_stolen") + "", true)
                    .setFooter("Stand: " + new Date(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200")
                    .build();
            return new MessageBuilder().setEmbed(messageEmbed).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MessageBuilder().build();
    }

    public static void verify(MessageReceivedEvent event) {
        if (event.getGuild() != null) {
            if (!event.getMember().getRoles().contains(event.getGuild().getRolesByName("verified", true).get(0))) {
                Content.addRole(event.getMember(), event.getGuild(), event.getGuild().getRolesByName("verified", true).get(0));
                event.getMessage().getChannel().sendMessage(
                        new MessageBuilder().append(event.getAuthor()).append(" ist nun verifiziert!").build()
                ).queue();
            } else {
                event.getMessage().getChannel().sendMessage(
                        new MessageBuilder().append(event.getAuthor()).append(" ist bereits verifiziert").build()
                ).queue();
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

}
