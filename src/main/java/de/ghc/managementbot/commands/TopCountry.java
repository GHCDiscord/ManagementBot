package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Country;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.SortedMap;
import java.util.TreeMap;

import static de.ghc.managementbot.content.Content.formatDate;
import static de.ghc.managementbot.content.Content.getRandomColor;

public class TopCountry extends Country implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        event.getChannel().sendTyping().queue();
        JSONArray arr = getCountryArray();
        SortedMap<Integer, JSONObject> avHCMap = new TreeMap<>();
        for (int i = 0; i < arr.length(); i++) {
            avHCMap.put(arr.getJSONObject(i).getInt("AvHC"), arr.getJSONObject(i));
        }
        String[] places = new String[10];
        int n = 0, pos = 10;
        for (JSONObject object : avHCMap.values()) {
            if (n >= (avHCMap.size() - 10)) {
                StringBuilder builder = new StringBuilder();
                places[pos-1] = builder.append(pos).append(": **").append(object.getString("CountryName")).append("** ").append(object.getInt("AvHC")).append("\n").toString();
                pos--;
            }
            n++;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < places.length; i++) {
            builder.append(places[i]);
        }
        event.getChannel().sendMessage(new EmbedBuilder().setColor(getRandomColor()).setTitle("Top 10 L\u00E4nder nach HC pro Spieler", null).setDescription(builder.toString()).setFooter("Stand: " + formatDate(), Content.GHCImageURL).build()).queue();
    }
}
