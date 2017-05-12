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
    public static final String addIP_error_noParameterAnswer = "addIP_error_noParameterAnswer";
    public static final String addIP_error_ipAlreadyExsistsAnswer = "addIP_error_ipAlreadyExsistsAnswer";
    public static final String addIP_error_exceptionAnswer = "addIP_error_exceptionAnswer";
    public static final String addIP_confirm_correctDataQuestionsAnswer = "addIP_confirm_correctDataQuestionsAnswer";
    public static final String addIP_confirm_correctDataParamsAnswer = "addIP_confirm_correctDataParamsAnswer";




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
            final Object a = new Object();
            synchronized (a) {
                try {
                    a.wait(3600000);
                } catch (InterruptedException ignore) {
                }
            }
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
