package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Data;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebhookHandler {
    public static void handleWebhook(Message message) {
        if (!message.isWebhookMessage() || message.getGuild().getIdLong() != Data.Guild.GHC)
            return;
        JSONObject object = new JSONObject(message.getContentDisplay());
        if (!object.has("event"))
            return;
        if (object.getString("event").equals("addip")) {
            onAddIP(object, message.getGuild());
        } else if (object.getString("event").equals("deleteip")) {
            onDeleteIP(object, message.getGuild());
        } else if (object.getString("event").equals("editIP")) {
            onEditIP(object, message.getGuild());
        }
    }

    private static void onAddIP(JSONObject object, Guild ghc) {
        AddIP.sendAddedIP(ghc.getMemberById(object.getString("addedby")));
    }

    private static void onDeleteIP(JSONObject object, Guild ghc) {
        sendReportThankMessage(object, "gelöscht", ghc);
    }

    private static void onEditIP(JSONObject object, Guild ghc) {
        sendReportThankMessage(object, "bearbeitet", ghc);
    }

    private static void sendReportThankMessage(JSONObject object, String action, Guild ghc) {
        if (!object.has("reportedby"))
            return;
        if (object.get("reportedby") == null || object.get("reportedby") == JSONObject.NULL)
            return;
        JSONArray array = object.getJSONArray("reportedby");
        switch (array.length()) {
            case 0:
                return;
            case 1:
                ghc.getTextChannelById(Data.Channel.de_hackersip).sendMessageFormat("Eine von <@%d> reportete IP wurde %s. Danke für deinen Report!", array.getLong(0), action).queue();
                break;
            case 2:
                ghc.getTextChannelById(Data.Channel.de_hackersip).sendMessageFormat("Eine von <@%d> und <@%d> reportete IP wurde %s. Danke für euren Report!",array.getLong(0), array.getLong(1), action).queue();
                break;
            default:
                StringBuilder builder = new StringBuilder().append("Eine von ");
                for (int i = 0; i < array.length() - 1; i++) {
                    builder.append("<@").append(array.getLong(i)).append(">, ");
                }
                builder.deleteCharAt(builder.length() - 2); //letztes Komma
                builder.append("und <@").append(array.getLong(array.length() - 1)).append("> reportete IP wurde ").append(action).append(". Danke für euren Report!");
                ghc.getTextChannelById(Data.Channel.de_hackersip).sendMessage(builder.toString()).queue();
                break;
        }
    }
}
