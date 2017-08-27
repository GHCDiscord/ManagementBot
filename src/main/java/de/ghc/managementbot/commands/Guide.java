package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

import static de.ghc.managementbot.content.Content.isModerator;

public class Guide implements Command {

    private String guide;

    public Guide(String guide) {
        this.guide = guide;
    }

    public static final String guild = " Alle wichtigen Informationen zu Gilden und deren Funktionsweise findest du unter http://forum.hackerz.online/viewtopic.php?f=12&t=78";

    public static final String faq = " Informationen und Erkl\u00E4rungen zum Spiel und seiner Funktionsweise findest du unter https://docs.google.com/document/d/18h_Ik023Ax9eGUxSCzVszhTask1y5ayP2TweVFNMdHE/pub";

    public static final String taktik = " Genaue Erkl\u00E4rungen zu verschiedenen Taktiken und Spielweisen finest du unter https://forum.hackerz.online/viewtopic.php?f=10&t=334";

    public static final String language = " `If you don't know the german language and you are not invited please leave. We are talking german in all channels here.`\nEnglish Discord: http://discord.gg/r7uHe3H";
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (isModerator(event.getMember())) {
            MessageBuilder builder = new MessageBuilder();
            event.getMessage().getMentionedUsers().forEach(builder::append);
            event.getChannel().sendMessage(builder.append(guide).build()).queue();
        }
        event.getMessage().delete().queue();
    }

    @Override
    public List<String> getCallers() {
        return Arrays.asList("!tut", "!guide", "!gilde", "!guild", "!guilds", "!taktik", "!de", "english", "!englisch", "!en");
    }

    @Override
    public boolean isCalled(String msg) {
        List<String> callers = getCallers();
        callers.replaceAll(String::toLowerCase);
        return callers.contains(msg.toLowerCase().split(" ")[0]);
    }

    @Override
    public Command createCommand(MessageReceivedEvent event) {
        String[] command = event.getMessage().getContent().split(" ");
        if (command[0].equalsIgnoreCase("!tut") || command[0].equalsIgnoreCase("!guide")) {
            return new Guide(Guide.faq);
        } else if (command[0].equalsIgnoreCase("!gilde") || command[0].equalsIgnoreCase("!guild") || command[0].equalsIgnoreCase("Guilds")) {
            return new Guide(Guide.guild);
        } else if (command[0].equalsIgnoreCase("!taktik")) {
            return new Guide(Guide.taktik);
        }else if (command[0].equalsIgnoreCase("!de") || command[0].equalsIgnoreCase("!english") || command[0].equalsIgnoreCase("!englisch") || command[0].equalsIgnoreCase("!en")) {
            return new Guide(Guide.language);
        }
        return Content.doNothing;
    }
}