package de.ghc.managementbot.listener;

import de.ghc.managementbot.content.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent();
        //output
        if (event.getGuild() == null) {
            System.out.printf("[Priv][%s] %s: %s\n", event.getChannel().getName(), event.getAuthor().getName(), msg);
        } else {
            System.out.printf("[%s][%s] %s: %s \n", event.getGuild().getName(),
                    event.getChannel().getName(), event.getAuthor().getName(), msg);
        }

        startCommand(event);
    }

    public static void startCommand(MessageReceivedEvent event) {
        getCommand(event).onMessageReceived(event);
    }

    private static Command getCommand(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent();
        String[] command = msg.split(" ");

        if (Content.getUserAddIPWithQuestionsMap().containsKey(event.getAuthor())) {
            Content.getUserAddIPWithQuestionsMap().get(event.getAuthor()).onMessageReceived(event);
        } else if (msg.equalsIgnoreCase("!stats")) {
            return new Stats();
        } else if (msg.equalsIgnoreCase(".c3po")) { //verify
            return new Verify();
        } else if (msg.equalsIgnoreCase("!topGuilds")) {
            return new TopGuilds();
        } else if (msg.equalsIgnoreCase("!help")) {
            return new Help();
        } else if (command[0].equalsIgnoreCase("!regeln") || command[0].equalsIgnoreCase("!rules")) {
            return new Rules();
        } else if (command[0].equalsIgnoreCase("!tut") || command[0].equalsIgnoreCase("!guide")) {
            return new Tutorial();
        } else if (command[0].equalsIgnoreCase("!addip") && command.length > 3) {
            return new AddIPsWithParams();
        } else if (command[0].equalsIgnoreCase("!addip") && command.length <= 3) {
            AddIPWithQuestions com = new AddIPWithQuestions(event.getAuthor());
            Content.addUser(event.getAuthor(), com);
            return com;
        }
        return new Command() {
            @Override
            public void onMessageReceived(MessageReceivedEvent event) {
            }
        };
    }
}
