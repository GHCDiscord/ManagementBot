package de.ghc.managementbot.content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static de.ghc.managementbot.content.Content.getRandomColor;

public class TopGuilds extends Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            URL url = new URL("http://hackerz.online/stats.json");
            String st = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
            JSONArray jsonArray = new JSONObject(st).getJSONArray("top_20_guilds");
            EmbedBuilder builder = new EmbedBuilder().setTitle("Top 10 Gilden:", "").setColor(getRandomColor());

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
                string.append(i + 1)
                        .append(". **")
                        .append(jsonArray.getJSONObject(i).getString("guild_name"))
                        .append(" **")
                        .append(jsonArray.getJSONObject(i).getString("mitigation"))
                        .append("\n");
            }
            event.getTextChannel().sendMessage(builder.setDescription(string.toString()).build()).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
