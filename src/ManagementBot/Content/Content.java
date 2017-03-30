package ManagementBot.Content;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Content {
    public static String getStats() {
        try {
            URL url = new URL("http://hackerz.online/stats.json");
            String s = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
            JSONObject jsonObject = new JSONObject(s).getJSONObject("game");
            return String.format("Blacklist: %s \nFehlgeschagene Bot-Attaken: %s \nErfolgreiche Bot-Attaken: %s \nVerbindungen: %s \nErfolgreich geknackte Passwörter: %s \nGestohlene Miner: %s \nGestohlene Wallets: %s",
                    jsonObject.getInt("blacklists"), jsonObject.get("bot_attacks_failed"), jsonObject.getInt("bot_attacks_success"), jsonObject.getInt("connections_to_target"), jsonObject.getInt("successful_cracked_passwords"), jsonObject.getInt("total_miners_stolen"), jsonObject.getInt("total_wallets_stolen"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
