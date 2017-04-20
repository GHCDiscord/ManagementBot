package ManagementBot.Content;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Country {

    protected static synchronized JSONArray getCountryArray() {
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://hackerz.online/country_competition.json");
        try {
            HttpResponse response = client.execute(get);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String data;
            StringBuilder line = new StringBuilder();
            while ((data = rd.readLine()) != null)
                line.append(data);
            return new JSONArray(line.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    protected static synchronized JSONObject getCountry (String wantedCountry) {
        JSONArray array = getCountryArray();
        if (wantedCountry.length() == 2) {
            for (int i = 0; i < array.length(); i++) {
                if (array.getJSONObject(i).getString("CountryCode").equalsIgnoreCase(wantedCountry)) {
                    return array.getJSONObject(i);
                }
            }
        } else {
            for (int i = 0; i < array.length(); i++) {
                if (array.getJSONObject(i).getString("CountryName").equalsIgnoreCase(wantedCountry)) {
                    return array.getJSONObject(i);
                }
            }
        }
        return null;
    }
}
