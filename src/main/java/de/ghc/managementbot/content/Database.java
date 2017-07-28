package de.ghc.managementbot.content;

import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.entity.User;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public abstract class Database {
  protected static final String url = "https://ghc-community.de";
  private static final String postIP = url + "/api/addip.php";
  private static final String getIP = url + "/api/jawasweißdenich";
  private static final String registerUser = url + "/api/registeruser.php";
  private static final String refresh = url + "/api/refreshaccount.php";
  private static final String stats = url + "/api/stats.php";
  private static final String expire = url + "/api/expireuser.php";

  private static HttpClient client = HttpClients.createDefault();

  protected static synchronized final String addIPtoDB(IPEntry entry) {
    List<NameValuePair> urlparams = new ArrayList<>();

    urlparams.add(new BasicNameValuePair("token", Secure.DBToken));
    urlparams.add(new BasicNameValuePair("ip", entry.getIP()));
    urlparams.add(new BasicNameValuePair("rep", entry.getRepopulation() + ""));
    urlparams.add(new BasicNameValuePair("desc", entry.getDescription()));
    urlparams.add(new BasicNameValuePair("miners", entry.getMiners() + ""));
    urlparams.add(new BasicNameValuePair("clan", entry.getGuildTag()));
    urlparams.add(new BasicNameValuePair("name", entry.getName()));
    urlparams.add(new BasicNameValuePair("discorduser", entry.getAddedBy().getId()));

    return makePOSTRequestAndHandleException(postIP, urlparams);
  }

  protected static synchronized final String registerNewUserInDB(User user) {
    client = HttpClients.createDefault();
    HttpPost post = new HttpPost(registerUser);
    List<NameValuePair> urlparams = new ArrayList<>();

    urlparams.add(new BasicNameValuePair("token", Secure.DBToken));
    urlparams.add(new BasicNameValuePair("name", new String(user.getUsername().getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"))));
    urlparams.add(new BasicNameValuePair("password", user.getPassword()));
    //urlparams.add(new BasicNameValuePair("email", user.getEMail())); //TODO wird nie gesetzt
    urlparams.add(new BasicNameValuePair("discorduser", user.getDiscordUser().getId()));

    return makePOSTRequestAndHandleException(registerUser, urlparams);
  }

  protected static synchronized final String refreshUser(net.dv8tion.jda.core.entities.User user) {
    List<NameValuePair> urlparams = new ArrayList<>();

    urlparams.add(new BasicNameValuePair("token", Secure.DBToken));
    urlparams.add(new BasicNameValuePair("discorduser", user.getId()));

    return makePOSTRequestAndHandleException(refresh, urlparams);
  }

  protected static synchronized final String getStats() {
    return makeGETRequestAndHandleException(stats)
            .replace("<br>", "\n ").replace("<title>Stats 1.0</title>", "").replace("<", "**").replace(">", "**");
  }
  protected static synchronized final JSONObject getStrings() {
    return new JSONObject(makeGETRequestAndHandleException("http://jonas.frikz.de/GHC/botCommands.json"));
  }

  public static synchronized final String expireUser(net.dv8tion.jda.core.entities.User user) {
    List<NameValuePair> params = new ArrayList<>();

    params.add(new BasicNameValuePair("token", Secure.DBToken));
    params.add(new BasicNameValuePair("discorduser", user.getId()));

    return makePOSTRequestAndHandleException(expire, params);
  }

  private static synchronized final String makeGETRequest(String url) throws IOException {
    HttpGet get = new HttpGet(url);
    return makeRequest(get);
  }

  private static synchronized final String makeGETRequestAndHandleException(String url) {
    try {
      return makeGETRequest(url);
    } catch (IOException e) {
      Content.sendException(e, Database.class);
      return e.getLocalizedMessage();
    }
  }

  private static synchronized final String makePOSTRequest(String url, List<NameValuePair> params) throws IOException {
    HttpPost post = new HttpPost(url);
    post.setEntity(new UrlEncodedFormEntity(params));
    return makeRequest(post);
  }

  private static synchronized final String makePOSTRequestAndHandleException(String url, List<NameValuePair> params) {
    try {
      return makePOSTRequest(url, params);
    } catch (IOException e) {
      Content.sendException(e, Database.class);
      return e.getLocalizedMessage();
    }
  }

  private static synchronized final String makeRequest(HttpRequestBase request) throws IOException {
    HttpClient client = HttpClients.createDefault();
    HttpResponse response = client.execute(request);
    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    StringBuilder site = new StringBuilder();
    for (String data; (data = reader.readLine()) != null;) {
      site.append(data);
    }
    return site.toString();
  }
}
