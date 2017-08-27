package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static de.ghc.managementbot.content.Content.formatDate;
import static de.ghc.managementbot.content.Content.getRandomColor;

public class Stats implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            URL url = new URL("http://hackerz.online/stats.json");
            String s = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
            JSONObject jsonObject = new JSONObject(s).getJSONObject("game");
            /*MessageEmbed messageEmbed = new EmbedBuilder()
                    .setTitle("Statistiken")
                    .addField("", "**Blacklist-Eintr\u00E4ge**" +  jsonObject.getInt("blacklists") + "", false)
                    .addField("" , "**Fehlgeschlagene Bot-Attaken**"+ jsonObject.getInt("bot_attacks_failed") +  "", false)
                    .addField("" ,"**Erfolgreiche Bot-Attaken**"+ jsonObject.getInt("bot_attacks_success") + "", true)
                    .addField("" ,"**Verbindungen**"+ jsonObject.getInt("connections_to_target") + "", false)
                    .addField("" ,"**Erfolgreich geknackte Passw\u00F6rter**"+ jsonObject.getInt("successful_cracked_passwords") + "", true)
                    .addField("", "**Gestohlene Miner**"+ jsonObject.getInt("total_miners_stolen") + "", false)
                    .addField("", "**Gestohlene Wallets**"+ jsonObject.getInt("total_wallets_stolen") + "", true)
                    .setFooter("Stand: " + Content.formateDate(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200")
                    .build();
            return new MessageBuilder().setEmbed(messageEmbed).build(); */
            String strg = new StringBuilder()
                    .append("**Blacklist-Eintr\u00E4ge:** ").append(jsonObject.getInt("blacklists"))
                    .append("\n**Fehlgeschlagene Bot-Attacken:** ").append(jsonObject.getInt("bot_attacks_failed"))
                    .append("\n**Erfolgreiche Bot-Attacken:** ").append(jsonObject.getInt("bot_attacks_success"))
                    .append("\n**Verbindungen:** ").append(jsonObject.getInt("connections_to_target"))
                    .append("\n**Erfolgreich geknackte Passw\u00F6rter:** ").append(jsonObject.getInt("successful_cracked_passwords"))
                    .append("\n**Gestohlene Miner:** ").append(jsonObject.getInt("total_miners_stolen"))
                    .append("\n**Gestohlene Wallets:** ").append(jsonObject.getInt("total_wallets_stolen"))
                    .toString();
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("Statistiken", null).setColor(getRandomColor())
                    .setDescription(strg)
                    .setFooter("Stand: " + formatDate(), Content.GHCImageURL).build()).queue();
        } catch (IOException e) {
            Content.sendException(e, Stats.class);
        }
    }

    @Override
    public List<String> getCallers() {
        return Collections.singletonList("!stats");
    }

    @Override
    public boolean isCalled(String msg) {
        List<String> callers = getCallers();
        String[] command = msg.split(" ");
        return callers.contains(command[0].toLowerCase()) && command.length == 1;
    }
}
