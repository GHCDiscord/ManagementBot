package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static de.ghc.managementbot.content.Content.formatDate;
import static de.ghc.managementbot.content.Content.getRandomColor;

public class TopGuilds implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            URL url = new URL("http://hackerz.online/stats.json");
            String st = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
            JSONArray jsonArray = new JSONObject(st).getJSONArray("top_20_guilds");
            EmbedBuilder builder = new EmbedBuilder().setTitle("Top 10 Gilden:", null).setColor(getRandomColor()).setFooter("Stand: " + formatDate(), Content.GHCImageURL);
            StringBuilder string = new StringBuilder();
            for (int i = 0; (i < jsonArray.length() && i < 10); i++) {
                string.append(i+1)
                        .append(". **")
                        .append(jsonArray.getJSONObject(i).getString("guild_name"))
                        .append(" **")
                        .append(jsonArray.getJSONObject(i).getString("mitigation"))
                        .append("\n");
            }
            event.getChannel().sendMessage(builder.setDescription(string.toString()).build()).queue();
        } catch (IOException e) {
            Content.sendException(e, TopGuilds.class);
        }
    }

    @Override
    public List<String> getCallers() {
        return Collections.singletonList("!topguilds");
    }
}
