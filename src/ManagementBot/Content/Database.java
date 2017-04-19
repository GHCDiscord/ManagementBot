package ManagementBot.Content;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

abstract class Database extends Command {
    protected static final String url = "http://bennet.deboben.de";
    private static final String postIP = url + "/api/addip.php";
    private static final String getIP = url + "/api/jawasweißdenich";
    private static final String registerUser = url + "/api/registeruser.php";
    private static final String refresh = url + "/api/refreshaccount.php";

    private static final String token = "l3y0G*204VQscqVmywsIM3tsRuaE152ph(E.0r.AWe4E.*AVIy";

    private static HttpClient client = new DefaultHttpClient(); //TODO Deprecated

    protected static String addIPtoDB(IPEntry entry) {
        System.out.println("add to db");
        HttpPost post = new HttpPost(postIP);

        List<NameValuePair> urlparams = new ArrayList<>();

        urlparams.add(new BasicNameValuePair("token", token));
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
            while ((data = rd.readLine()) != null)
                line += data;
            System.out.println(line);
            return line;
        } catch (Exception e) {
            return e.toString();
        }
    }

    protected String registerNewUserInDB(User user) {
        HttpPost post = new HttpPost(registerUser);
        List<NameValuePair> urlparams = new ArrayList<>();

        urlparams.add(new BasicNameValuePair("token", token));
        urlparams.add(new BasicNameValuePair("name", user.getUsername()));
        urlparams.add(new BasicNameValuePair("password", user.getPassword()));
        urlparams.add(new BasicNameValuePair("email ", user.getEMail())); //TODO wird nie gesetzt
        urlparams.add(new BasicNameValuePair("discorduser", user.getDiscordUser().getId()));


        try {
            post.setEntity(new UrlEncodedFormEntity(urlparams));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String data, line = "";
            while ((data = rd.readLine()) != null)
                line += data;
            System.out.println(line);
            return line;
        } catch (Exception e) {
            return e.toString();
        }
    }

    protected String refreshUser(net.dv8tion.jda.core.entities.User user) {
        HttpPost post = new HttpPost(refresh);
        List<NameValuePair> urlparams = new ArrayList<>();

        urlparams.add(new BasicNameValuePair("token", token));
        urlparams.add(new BasicNameValuePair("discorduser", user.getId()));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlparams));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String data, line = "";
            while ((data = rd.readLine()) != null)
                line += data;
            System.out.println(line);
            return line;
        } catch (Exception e) {
            return e.toString();
        }
    }
}
