package de.ghc.managementbot.content;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static de.ghc.managementbot.content.Content.isModerator;

public class Guide implements Command{

    private String guide;

    public Guide(String guide) {
        this.guide = guide;
    }

    public static final String guild = " Alle wichtigen Informationen zu Gilden und deren Funktionsweise findest du unter http://forum.hackerz.online/viewtopic.php?f=12&t=78";

    public static final String faq = " Informationen und Erkl\u00E4rungen zum Spiel und seiner Funktionsweise findest du unter https://docs.google.com/document/d/18h_Ik023Ax9eGUxSCzVszhTask1y5ayP2TweVFNMdHE/pub";

    public static final String taktik = " Genaue Erkl\u00E4rungen zu verschiedenen Taktiken und Spielweisen finest du unter https://forum.hackerz.online/viewtopic.php?f=10&t=334";

    public static final String language = " `If you don't know the german language and you are not invited please leave. We are talking german in all channels here.`\n English Discord: http://discord.gg/r7uHe3H";
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (isModerator(event.getMember())) {
            MessageBuilder builder = new MessageBuilder();
            event.getMessage().getMentionedUsers().forEach(builder::append);
            event.getChannel().sendMessage(builder.append(guide).build()).queue();
        }
        new Thread(new DeleteMessageThread(0, event.getMessage())).start();
    }
}
