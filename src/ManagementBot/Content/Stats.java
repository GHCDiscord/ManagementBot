package ManagementBot.Content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import static ManagementBot.Content.Content.getRandomColor;

public class Stats implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
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
                    .append("\n**Fehlgeschlagene Bot-Attacken:** ").append(jsonObject.getInt("bot_attacks_failed"))
                    .append("\n**Erfolgreiche Bot-Attacken:** ").append(jsonObject.getInt("bot_attacks_success"))
                    .append("\n**Verbindungen:** ").append(jsonObject.getInt("connections_to_target"))
                    .append("\n**Erfolgreich geknackte Passwörter:** ").append(jsonObject.getInt("successful_cracked_passwords"))
                    .append("\n**Gestohlene Miner:** ").append(jsonObject.getInt("total_miners_stolen"))
                    .append("\n**Gestohlene Wallets:** ").append(jsonObject.getInt("total_wallets_stolen"))
                    .toString();
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("Statistiken").setColor(getRandomColor())
                    .setDescription(strg)
                    .setFooter("Stand: " + new Date(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200").build()).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
