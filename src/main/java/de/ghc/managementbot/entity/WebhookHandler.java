package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebhookHandler {
    public static void hadleWebhook(Message message) {
        if (!message.isWebhookMessage())
            return;
        JSONObject object = new JSONObject(message.getContent());
        if (!object.has("event"))
            return;
        if (object.getString("event").equals("addip")) {
            onAddIP(object);
        } else if (object.getString("event").equals("deleteip")) {
            onDeleteIP(object);
        } else if (object.getString("event").equals("editIP")) {
            onEditIP(object);
        }
    }

    private static void onAddIP(JSONObject object) {
        AddIP.sendAddedIP(Content.getGhc().getMemberById(object.getString("addedby")));
    }

    private static void onDeleteIP(JSONObject object) {
        sendReportThankMessage(object, "gelöscht");
    }

    private static void onEditIP(JSONObject object) {
        sendReportThankMessage(object, "bearbeitet");
    }

    private static void sendReportThankMessage(JSONObject object, String action) {
        if (!object.has("reportedby"))
            return;
        if (object.get("reportedby") == null || object.get("reportedby") == JSONObject.NULL)
            return;
        JSONArray array = object.getJSONArray("reportedby");
        switch (array.length()) {
            case 0:
                return;
            case 1:
                Content.getGhc().getTextChannelById(Data.Channel.hackersip).sendMessageFormat("Eine von <@%d> reportete IP wurde %s. Danke für deinen Report!", array.getLong(0), action).queue();
                break;
            case 2:
                Content.getGhc().getTextChannelById(Data.Channel.hackersip).sendMessageFormat("Eine von <@%d> und <@%d> reportete IP wurde %s. Danke für euren Report!",array.getLong(0), array.getLong(1), action).queue();
                break;
            default:
                StringBuilder builder = new StringBuilder().append("Eine von ");
                for (int i = 0; i < array.length() - 1; i++) {
                    builder.append("<@").append(array.getLong(i)).append(">, ");
                }
                builder.deleteCharAt(builder.length() - 2); //letztes Komma
                builder.append("und <@").append(array.getLong(array.length() - 1)).append("> reportete IP wurde ").append(action).append(". Danke für euren Report!");
                Content.getGhc().getTextChannelById(Data.Channel.hackersip).sendMessage(builder.toString()).queue();
                break;
        }
    }
}
