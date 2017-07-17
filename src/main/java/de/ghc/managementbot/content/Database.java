package de.ghc.managementbot.content;

import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.entity.User;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

  protected static synchronized String addIPtoDB(IPEntry entry) {
    client = HttpClients.createDefault();
    HttpPost post = new HttpPost(postIP);

    List<NameValuePair> urlparams = new ArrayList<>();

    urlparams.add(new BasicNameValuePair("token", Secure.DBToken));
    urlparams.add(new BasicNameValuePair("ip", entry.getIP()));
    urlparams.add(new BasicNameValuePair("rep", entry.getRepopulation() + ""));
    urlparams.add(new BasicNameValuePair("desc", entry.getDescription()));
    urlparams.add(new BasicNameValuePair("miners", entry.getMiners() + ""));
    urlparams.add(new BasicNameValuePair("clan", entry.getGuildTag()));
    urlparams.add(new BasicNameValuePair("name", entry.getName()));
    urlparams.add(new BasicNameValuePair("discorduser", entry.getAddedBy().getId()));

    try {
      post.setEntity(new UrlEncodedFormEntity(urlparams));
      HttpResponse response = client.execute(post);
      BufferedReader rd = new BufferedReader(
          new InputStreamReader(response.getEntity().getContent()));
      String data, line = "";
      while ((data = rd.readLine()) != null) {
        line += data;
      }
      System.out.println(line);
      return line;
    } catch (Exception e) {
      Content.getGhc().getTextChannelById(Data.botLog).sendMessage("Database: IOException: " + e.getLocalizedMessage()).queue();
      return e.toString();
    }
  }

  protected static synchronized String registerNewUserInDB(User user) {
    System.out.println("start register");
    client = HttpClients.createDefault();
    HttpPost post = new HttpPost(registerUser);
    List<NameValuePair> urlparams = new ArrayList<>();

    urlparams.add(new BasicNameValuePair("token", Secure.DBToken));
    urlparams.add(new BasicNameValuePair("name", new String(user.getUsername().getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"))));
    urlparams.add(new BasicNameValuePair("password", user.getPassword()));
    //urlparams.add(new BasicNameValuePair("email", user.getEMail())); //TODO wird nie gesetzt
    urlparams.add(new BasicNameValuePair("discorduser", user.getDiscordUser().getId()));


    try {
      post.setEntity(new UrlEncodedFormEntity(urlparams));
      HttpResponse response = client.execute(post);
      System.out.println(response.getStatusLine().getStatusCode());
      System.out.println(response.getStatusLine().getReasonPhrase());
      BufferedReader rd = new BufferedReader(
          new InputStreamReader(response.getEntity().getContent()));
      String data, line = "";
      while ((data = rd.readLine()) != null) {
        line += data;
      }
      System.out.println(line);
      return line;
    } catch (Exception e) {
      Content.getGhc().getTextChannelById(Data.botLog).sendMessage("Database: IOException: " + e.getLocalizedMessage()).queue();
      return e.toString();
    } finally {
      System.out.println("finish register");
    }
  }

  protected static synchronized String refreshUser(net.dv8tion.jda.core.entities.User user) {
    HttpPost post = new HttpPost(refresh);
    client = HttpClients.createDefault();
    List<NameValuePair> urlparams = new ArrayList<>();

    urlparams.add(new BasicNameValuePair("token", Secure.DBToken));
    urlparams.add(new BasicNameValuePair("discorduser", user.getId()));

    try {
      post.setEntity(new UrlEncodedFormEntity(urlparams));
      HttpResponse response = client.execute(post);
      BufferedReader rd = new BufferedReader(
          new InputStreamReader(response.getEntity().getContent()));
      String data, line = "";
      while ((data = rd.readLine()) != null) {
        line += data;
      }
      System.out.println(line);
      return line;
    } catch (Exception e) {
      Content.getGhc().getTextChannelById(Data.botLog).sendMessage("Database: IOException: " + e.getLocalizedMessage()).queue();
      return e.toString();
    }
  }

  protected static synchronized String getStats() {
    HttpGet get = new HttpGet(stats);
    client = HttpClients.createDefault();
    try {
      HttpResponse response = client.execute(get);
      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuilder stats = new StringBuilder();
      String data;
      while ((data = rd.readLine()) != null) {
        stats.append(data);
      }
      return stats.toString().replace("<br>", "\n ").replace("<title>Stats 1.0</title>", "").replace("<", "**").replace(">", "**");
    } catch (IOException e) {
      Content.getGhc().getTextChannelById(Data.botLog).sendMessage("Database: IOException: " + e.getLocalizedMessage()).queue();
      return null;
    }
  }
  protected static synchronized JSONObject getStrings() {
    HttpClient client = HttpClients.createDefault();
    HttpGet get = new HttpGet(url + "botCommands.json");
    try {
      HttpResponse response = client.execute(get);
      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuilder strings = new StringBuilder();
      String data;
      while ((data = rd.readLine()) != null) {
        strings.append(data);
      }
      return new JSONObject(strings.toString());
    } catch (IOException e) {
      Content.getGhc().getTextChannelById(Data.botLog).sendMessage("Database: IOException: " + e.getLocalizedMessage()).queue();
    }
    return null;
  }

  public static synchronized String expireUser(net.dv8tion.jda.core.entities.User user) {
    HttpClient client = HttpClients.createDefault();
    HttpPost post = new HttpPost(expire);
    List<NameValuePair> params = new ArrayList<>();

    params.add(new BasicNameValuePair("token", Secure.DBToken));
    params.add(new BasicNameValuePair("discorduser", user.getId()));

    try {
      post.setEntity(new UrlEncodedFormEntity(params));
      HttpResponse response = client.execute(post);
      BufferedReader rd = new BufferedReader(
              new InputStreamReader(response.getEntity().getContent()));
      String data, line = "";
      while ((data = rd.readLine()) != null) {
        line += data;
      }
      return line;
    } catch (IOException e) {
      Content.getGhc().getTextChannelById(Data.botLog).sendMessage("Database: IOException: " + e.getLocalizedMessage()).queue();
      return e.getMessage();
    }
  }
}
