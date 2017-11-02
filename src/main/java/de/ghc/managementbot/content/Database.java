package de.ghc.managementbot.content;

import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.entity.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

public abstract class Database {
  protected static final String url = "http://ghc-community.de";
  private static final String postIP = "addip";
  private static final String getIP = "getip";
  private static final String registerUser = "registeruser";
  private static final String refresh = "refreshuser";
  private static final String stats = url + "/api/stats.php";
  private static final String expire = "banuser";

  protected static synchronized final JSONObject addIPtoDB(IPEntry entry) {

    JSONObject variables = new JSONObject();
    variables.put("token", Secure.DBToken);
    variables.put("ip", entry.getIP());
    variables.put("rep", entry.getRepopulation());
    variables.put("desc", entry.getDescription());
    variables.put("miners", entry.getMiners());
    variables.put("clan", entry.getGuildTag());
    variables.put("name", entry.getName());
    variables.put("discorduser", entry.getAddedBy().getId());
    variables.put("update", entry.getUpdate());

    return makeRequestAndHandleResponse(postIP, variables);
  }

  protected static synchronized final JSONObject registerNewUserInDB(User user) {
    JSONObject object = new JSONObject();
    object.put("token", Secure.DBToken);
    object.put("name", user.getUsername());
    object.put("password", user.getPassword());
    object.put("discorduser", user.getDiscordUser().getId());

    return makeRequestAndHandleResponse(registerUser, object);
  }

  protected static synchronized final JSONObject refreshUser(net.dv8tion.jda.core.entities.User user) {

    JSONObject object = new JSONObject();
    object.put("token", Secure.DBToken);
    object.put("discorduser", user.getId());

    return makeRequestAndHandleResponse(refresh, object);
  }

  protected static synchronized final String getStats() {
    try {
      return makeRequest(new HttpGet(stats))
              .replace("<br>", "\n ").replace("<title>Stats 1.0</title>", "").replace("<", "**").replace(">", "**");
    } catch (IOException e) {
      Content.sendException(e, Database.class);
      return e.getLocalizedMessage();
    }
  }
  protected static synchronized final JSONObject getStrings() {
    try {
      return new JSONObject(makeRequest(new HttpGet(""/*"http://jonas.frikz.de/GHC/botCommands.json"*/)));
    } catch (IOException e) {
      Content.sendException(e, Database.class);
      return null;
    }
  }

  public static synchronized final JSONObject expireUser(net.dv8tion.jda.core.entities.User user) {
    JSONObject object = new JSONObject();
    object.put("token", Secure.DBToken);
    object.put("discorduser", user.getId());

    return makeRequestAndHandleResponse(expire, object);
  }

  protected static final IPEntry getIP(long IPID) {
    JSONObject object = new JSONObject();
    object.put("token", Secure.DBToken);
    object.put("IPID", IPID);
    return generateIpEntry(makeRequestAndHandleResponse(getIP, object));
  }

  private static final IPEntry generateIpEntry(JSONObject object) {
    if (object == null)
      return null;
    if (!object.has("IPFunde"))
      return null;
    if (object.get("IPFunde") instanceof JSONObject) {
      return generateIpEntry0(object.getJSONObject("IPFunde"));
    } else if (object.get("IPFunde") instanceof JSONArray) {
      return generateIpEntry0(object.getJSONArray("IPFunde").getJSONObject(0)); //TODO
    }
    return null;
  }

  private static final IPEntry generateIpEntry0(JSONObject ip) {
    IPEntry entry = new IPEntry(ip.getString("IP"));
    entry.setName(ip.getString("Name"));
    entry.setRepopulation(ip.getInt("Rep"));
    entry.setMiners(ip.getInt("Miners"));
    entry.setGuildTag(ip.getString("Clan"));
    entry.setDescription(ip.getString("Desc"));
    return entry;
  }

  private static synchronized final String makeRequest(HttpRequestBase request) throws IOException {
    HttpClient client = HttpClients.createDefault();
    request.setHeader("User-Agent", "GHC-Bot");
    HttpResponse response = client.execute(request);
    System.out.println(response.getStatusLine());
    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    StringBuilder site = new StringBuilder();
    for (String data; (data = reader.readLine()) != null;) {
      site.append(data);
    }
    return site.toString();
  }

  private static synchronized final JSONObject makeRequestAndHandleResponse(String function, JSONObject variables) {
    try {
      String object = URLEncoder.encode(variables.toString());
      System.out.println(String.format("%s/index.php/apibot/%s/%s", url, function, object));
      HttpGet get = new HttpGet(String.format("%s/index.php/apibot/%s/%s", url, function, object));
      String request = makeRequest(get);
      System.out.println(request);
      try {
        return new JSONObject(request);
      }catch (JSONException e) {
        return null;
      }
    } catch (IOException e) {
      Content.sendException(e, Database.class);
      return new JSONObject().put("error", true).put("msgName", e.getLocalizedMessage());
    }
  }
}
