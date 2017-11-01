package de.ghc.managementbot.content;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class Strings extends Database {

    public static final String addIP_field_inputIp = "addIP_field_inputIp";
    public static final String addIP_field_inputName = "addIP_field_inputName";
    public static final String addIP_field_inputMinerCount = "addIP_field_inputMinerCount";
    public static final String addIP_field_inputReputation = "addIP_field_inputReputation";
    public static final String addIP_field_inputGuildTag = "addIP_field_inputGuildTag";
    public static final String addIP_error_noParameter = "addIP_error_noParameter";
    public static final String addIP_error_ipAlreadyExsists = "addIP_error_ipAlreadyExsists";
    public static final String addIP_error_exception = "addIP_error_exception";
    public static final String addIP_confirm_correctDataQuestions = "addIP_confirm_correctDataQuestions";
    public static final String addIP_confirm_correctDataParamsAnswer = "addIP_confirm_correctDataParamsAnswer";
    public static final String addIP_success_addedIp = "addIP_success_addedIp";

    public static final String register_success_addedAccount = "register_success_addedAccount";
    public static final String register_error_multipleWords = "register_error_multipleWords";
    public static final String register_error_unexpectedChars = "register_error_unexpectedChars";
    public static final String register_error_noUsername = "register_error_noUsername";
    public static final String register_error_accountAlreadyExists = "register_error_accountAlreadyExists";
    public static final String register_error_usernameIsAlreadyTaken = "register_error_usernameIsAlreadyTaken";

    public static final String stats_land_field_averageHC = "stats_land_field_averageHC";
    public static final String stats_land_field_totalHC = "stats_land_field_totalHC";
    public static final String stats_land_field_stolenMinersCount = "stats_land_field_stolenMinersCount";
    public static final String stats_land_field_hackedPasswordsCount = "stats_land_field_hackedPasswordsCount";
    public static final String stats_land_field_blacklistEntriesCount = "stats_land_field_blacklistEntriesCount";
    public static final String stats_land_field_completedMissions = "stats_land_field_completedMissions";
    public static final String stats_land_error_unexpectedCountry = "stats_land_error_unexpectedCountry";

    public static final String regeln_success = "regeln_success";

    public static final String help_header_ghcBot = "help_header_ghcBot";
    public static final String help_userCommands = "help_userCommands";
    public static final String help_verifiedCommands = "help_verifiedCommands";
    public static final String help_modCommands = "help_modCommands";
    public static final String help_verifiedMessage = "help_verifiedMessage";
    public static final String help_addIPParams = "help_addIPParams";

    public static final String refresh_success = "refresh_success";
    public static final String refresh_error_userNotFound = "refresh_error_userNotFound";

    public static final String stats_DB_title = "stats_DB_title";
    public static final String stats_DB_field_date = "public static final String stats_DB_field_date";
    public static final String stats_DB_field_updated = "stats_DB_field_updated";

    public static final String topCountry_title = "topCountry_title";

    public static final String topGuilds_title = "topGuilds_title";

    public static final String verify_success = "verify_sucess";
    public static final String verify_error = "verify_error";


    private static final Object lock = new Object();

    private static volatile Map<String, String> strings;

    public static String getString(String id) {
        synchronized (lock) {
            return strings.get(id);
        }
    }

    public static String getAndReplaceString(String id, Map<String, Object> replacements) {
        String command = getString(id);
        for (String s : replacements.keySet()) {
            command = command.replace(s, replacements.get(s).toString());
        }
        return command;
    }
    private static Thread makeStringsThread = new Thread(() -> {
        while (true) {
            JSONObject stringJSON = getStrings();
            synchronized (lock) {
                if (stringJSON != null && stringJSON != JSONObject.NULL) {
                    strings = new HashMap<>();
                    for (String command : stringJSON.keySet()) {
                        JSONObject commandStrings = stringJSON.getJSONObject(command);
                        for (String commandStringName : commandStrings.keySet()) {
                            String commandStringAnswer = commandStrings.getJSONObject(commandStringName).getString("answer");
                            strings.put(command + "_" + commandStringName, commandStringAnswer);
                        }
                    }
                }
            }
            try {
                Thread.sleep(3600000);
            } catch (InterruptedException e) {
                return;
            }
        }
    });

    public static void start() {
        makeStringsThread.start();
    }
}
