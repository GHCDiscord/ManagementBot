package de.ghc.managementbot.content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import static de.ghc.managementbot.content.Content.getRandomColor;

public class CountryStats extends Country implements Command {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        event.getChannel().sendTyping().queue();
        JSONArray array = getCountryArray();
        JSONObject country = getCountry(event.getMessage().getContent().substring(7));
        if (country != null) {
            EmbedBuilder builder = new EmbedBuilder().setColor(getRandomColor())
                    .setFooter("Stand: " + new Date(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200")
                    //.setThumbnail("http://hackerz.online/public/img/country/" + country.getString("CountryCode").toLowerCase() + ".png")
                    .setAuthor(country.getString("CountryName"), "http://hackerz.online/country_competition", "http://hackerz.online/public/img/country/" + country.getString("CountryCode").toLowerCase() + ".png")
                    .addField("Durchschnittliche HC", country.getInt("AvHC") + "", true)
                    .addField("HC Insgesammt", country.getInt("TotalHC") + "", true)
                    .addField("Gestohlene Miner", country.getInt("TotalSteals") + "", true)
                    .addField("Erfolgreich geknackte Passwörter", country.getInt("TotalCracks") + "", true)
                    //.addField("Blackbits", germany.get("TotalBlacklists") + "", true)
                    .addField("Abgeschlossene Missionen", country.getInt("TotalMissionsOK") + "", true);
            event.getChannel().sendMessage(builder.build()).queue();
        } else
            event.getChannel().sendMessage("Land wurde nicht gefunden!").queue();
    }
}
