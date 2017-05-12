package de.ghc.managementbot.content;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
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
    public static final String addIP_confirm_correctDataParams = "addIP_confirm_correctDataParams";

    public static final String register_success_addedAccount = "register_success_addedAccount";
    public static final String register_error_multipleWords = "register_error_multipleWords";
    public static final String register_error_unexpectedChars = "register_error_unexpectedChars";
    public static final String register_error_noUsername = "register_error_noUsername";
    public static final String register_error_accountAlreadyExists = "register_error_accountAlreadyExists";
    public static final String register_error_usernameIsAlreadyTaken = "register_error_usernameIsAlreadyTaken";


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
            try {
                Thread.sleep(3600000);
            } catch (InterruptedException ignore) {}
            JSONObject stringJSON = getStrings();
            synchronized (lock) {
                if (stringJSON != null) {
                    strings = new HashMap<>();
                    Iterator<String> commands = stringJSON.keys();
                    while (commands.hasNext()) {
                        String command = commands.next();
                        JSONObject commandStrings = stringJSON.getJSONObject(command);
                        Iterator<String> commandStringNames = commandStrings.keys();
                        while (commandStringNames.hasNext()) {
                            String commandStringName = commandStringNames.next();
                            String commandStringAnswer = commandStrings.getJSONObject(commandStringName).getString("answer");
                            strings.put(command + "_" + commandStringName, commandStringAnswer);
                        }
                    }
                }
            }
        }
    });

    public static void start() {
        makeStringsThread.start();
    }
}
