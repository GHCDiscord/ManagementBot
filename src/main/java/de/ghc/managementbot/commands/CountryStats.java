package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Country;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import static de.ghc.managementbot.content.Content.getImageColor;

public class CountryStats extends Country implements Command {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        event.getChannel().sendTyping().queue();
        JSONArray array = getCountryArray();
        JSONObject country = getCountry(event.getMessage().getContent().substring(7));
        if (country != null) {
            String url = "http://hackerz.online/public/img/country/" + country.getString("CountryCode").toLowerCase() + ".png";
            EmbedBuilder builder = new EmbedBuilder().setColor(getImageColor(url))
                    .setFooter("Stand: " + new Date(), Content.GHCImageURL)
                    //.setThumbnail("http://hackerz.online/public/img/country/" + country.getString("CountryCode").toLowerCase() + ".png")
                    .setAuthor(country.getString("CountryName"), "http://hackerz.online/country_competition", url)
                    .addField("Durchschnittliche HC", country.getInt("AvHC") + "", true)
                    .addField("HC Insgesamt", country.getInt("TotalHC") + "", true)
                    .addField("Gestohlene Miner", country.getInt("TotalSteals") + "", true)
                    .addField("Erfolgreich geknackte Passw\u00F6rter", country.getInt("TotalCracks") + "", true)
                    .addField("Blacklist-Eintr\u00E4ge", country.get("TotalBlacklists") + "", true)
                    .addField("Abgeschlossene Missionen", country.getInt("TotalMissionsOK") + "", true);
            event.getChannel().sendMessage(builder.build()).queue();
        } else
            event.getChannel().sendMessage("Land wurde nicht gefunden!").queue();
    }
}
